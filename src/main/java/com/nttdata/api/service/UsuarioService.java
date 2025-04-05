package com.nttdata.api.service;

import com.nttdata.api.controller.mapper.UsuarioMapper;
import com.nttdata.api.controller.dtos.TelefonoDto;
import com.nttdata.api.controller.dtos.UsuarioRequestDto;
import com.nttdata.api.controller.dtos.UsuarioResponseDto;
import com.nttdata.api.entity.TelefonoEntity;
import com.nttdata.api.entity.UsuarioEntity;
import com.nttdata.api.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private final EntityManager entityManager;

    private static final String  USUARIO_NO_ENCONTRADO ="Usuario no encontrado";

    private static final String CORREO_NO_PERTENECE_AL_USUARIO = "El correo no pertenece al usuario";

    public UsuarioService(UsuarioRepository usuarioRepository,  PasswordEncoder passwordEncoder,EntityManager entityManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }


    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> getAll() {
        log.info("Buscando todos los usuarios activos");

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("activarFiltro").setParameter("isActivo", true);

        List<UsuarioEntity> usuariosActivos = usuarioRepository.findAll();
        session.disableFilter("activarFiltro");

        log.info("Usuarios activos encontrados: {}", usuariosActivos.size());
        return usuariosActivos.stream().map(user -> {
            List<TelefonoDto> telefonoDtos = user.getTelefonoEntities().stream()
                    .map(UsuarioMapper::mapToTelefonoDto)
                    .collect(Collectors.toList());

                return UsuarioMapper.mapEntityToUsuarioResponseDto(user, telefonoDtos);
            }).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDto> getUsuarioById(UUID id) {
        log.info("Buscando usuario por id {}" , id);

        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(USUARIO_NO_ENCONTRADO));

        List<TelefonoDto> telefonoDtos = usuarioEntity.getTelefonoEntities().stream()
                .map(UsuarioMapper::mapToTelefonoDto)
                .toList();

        log.info("Usuario encontrado: {}", usuarioEntity.getNombre());
        return Optional.of(UsuarioMapper.mapEntityToUsuarioResponseDto(usuarioEntity, telefonoDtos));
    }

    @Transactional
    public UsuarioResponseDto updateUsuario(UUID id, UsuarioRequestDto request) {
        log.info("actualizando datos del usuario {}" , id);

        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(USUARIO_NO_ENCONTRADO));

        if(!usuarioEntity.getCorreo().equals(request.getCorreo())){
            throw new IllegalArgumentException(CORREO_NO_PERTENECE_AL_USUARIO);
        }

        AtomicInteger index = new AtomicInteger();
        List<TelefonoEntity> telefonoEntities = request.getTelefonos().stream()
                .map(telefonoDto -> {
                    TelefonoEntity telefonoEntity = new TelefonoEntity();
                    if (index.get() < usuarioEntity.getTelefonoEntities().size()) {
                        telefonoEntity.setId(usuarioEntity.getTelefonoEntities().get(index.getAndIncrement()).getId());
                    }
                    telefonoEntity.setCodigoCiudad(telefonoDto.getCodigoCiudad());
                    telefonoEntity.setNumero(telefonoDto.getNumero());
                    telefonoEntity.setCodigoPais(telefonoDto.getCodigoPais());
                    telefonoEntity.setUsuarioEntity(usuarioEntity);
                    return telefonoEntity;
                })
                .toList();

        usuarioEntity.getTelefonoEntities().clear();
        usuarioEntity.getTelefonoEntities().addAll(telefonoEntities);
        usuarioEntity.setActivo(request.getActivo());
        usuarioEntity.setNombre(request.getNombre());
        usuarioEntity.setModificado(LocalDateTime.now());

        UsuarioEntity updatedUsuarioEntity = usuarioRepository.save(usuarioEntity);
        List<TelefonoDto> telefonoDto = updatedUsuarioEntity.getTelefonoEntities().stream()
                .map(UsuarioMapper::mapToTelefonoDto)
                .toList();

        log.info("Usuario actualizado: {}", updatedUsuarioEntity.getNombre());
        return UsuarioMapper.mapEntityToUsuarioResponseDto(updatedUsuarioEntity, telefonoDto);
    }


    @Transactional
    public UsuarioResponseDto patchUsuario(UUID id,UsuarioRequestDto request) {
            log.info("actualizando password del usuario {}" , request.getCorreo());

            UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(USUARIO_NO_ENCONTRADO));

            if(!usuarioEntity.getCorreo().equals(request.getCorreo())){
                throw new IllegalArgumentException(CORREO_NO_PERTENECE_AL_USUARIO);
            }

            usuarioEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            usuarioEntity.setModificado(LocalDateTime.now());
            UsuarioEntity updatedUsuarioEntity = usuarioRepository.save(usuarioEntity);
            List<TelefonoDto> telefonoDtos = updatedUsuarioEntity.getTelefonoEntities().stream()
                    .map(UsuarioMapper::mapToTelefonoDto)
                    .collect(Collectors.toList());

            log.info("Usuario actualizado: {}", updatedUsuarioEntity.getNombre());
            return UsuarioMapper.mapEntityToUsuarioResponseDto(updatedUsuarioEntity, telefonoDtos);

    }

    @Transactional
    public void deleteUsuario(UUID id) {
        log.info("Eliminando usuario por id {}" , id);

        UsuarioEntity usuarioEntity = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(USUARIO_NO_ENCONTRADO));

        log.info("Usuario encontrado: {}", usuarioEntity.getNombre());
        usuarioRepository.delete(usuarioEntity);
    }

}