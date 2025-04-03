package com.nttdata.api.services;

import com.nttdata.api.controllers.mapper.UsuarioMapper;
import com.nttdata.api.controllers.dtos.TelefonoDto;
import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import com.nttdata.api.controllers.dtos.UsuarioResponseDto;
import com.nttdata.api.entities.TelefonoEntity;
import com.nttdata.api.entities.UsuarioEntity;
import com.nttdata.api.repositories.UsuarioRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final EntityManager entityManager;

    public UsuarioService(UsuarioRepository usuarioRepository, JwtService jwtService, PasswordEncoder passwordEncoder,EntityManager entityManager) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
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
    public Optional<UsuarioResponseDto> getUserById(UUID id) {
        log.info("Buscando usuario por id {}" , id);

        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<TelefonoDto> telefonoDtos = usuarioEntity.getTelefonoEntities().stream()
                .map(UsuarioMapper::mapToTelefonoDto)
                .toList();

        log.info("Usuario encontrado: {}", usuarioEntity.getNombre());
        return Optional.of(UsuarioMapper.mapEntityToUsuarioResponseDto(usuarioEntity, telefonoDtos));
    }

    @Transactional
    public UsuarioResponseDto updateUsuario(UUID id, UsuarioRequestDto usuarioRequestDto) {
        log.info("actualizando datos del usuario {}" , id);

        UsuarioEntity usuarioEntity = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        AtomicInteger index = new AtomicInteger();
        List<TelefonoEntity> telefonoEntities = usuarioRequestDto.getTelefonos().stream()
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
        usuarioEntity.setActivo(usuarioRequestDto.getActivo());
        usuarioEntity.setNombre(usuarioRequestDto.getNombre());
        usuarioEntity.setModificado(LocalDateTime.now());

        UsuarioEntity updatedUsuarioEntity = usuarioRepository.save(usuarioEntity);
        List<TelefonoDto> telefonoDto = updatedUsuarioEntity.getTelefonoEntities().stream()
                .map(UsuarioMapper::mapToTelefonoDto)
                .toList();

        log.info("Usuario actualizado: {}", updatedUsuarioEntity.getNombre());
        return UsuarioMapper.mapEntityToUsuarioResponseDto(updatedUsuarioEntity, telefonoDto);
    }


    @Transactional
    public UsuarioResponseDto patchUsuario(UUID id, UsuarioRequestDto updates) {
        log.info("actualizando password del usuario {}" , id);

        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (updates.getPassword() != null) {
            usuarioEntity.setPassword(passwordEncoder.encode(updates.getPassword()));
        }


        UsuarioEntity updatedUsuarioEntity = usuarioRepository.save(usuarioEntity);
        List<TelefonoDto> telefonoDtos = updatedUsuarioEntity.getTelefonoEntities().stream()
                .map(UsuarioMapper::mapToTelefonoDto)
                .collect(Collectors.toList());

        log.info("Usuario actualizado: {}", updatedUsuarioEntity.getNombre());
        return UsuarioMapper.mapEntityToUsuarioResponseDto(updatedUsuarioEntity, telefonoDtos);
    }

    @Transactional
    public void deleteUser(UUID id) {
        log.info("Eliminando usuario por id {}" , id);

        UsuarioEntity usuarioEntity = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("usuario no encontrado"));

        log.info("Usuario encontrado: {}", usuarioEntity.getNombre());
        usuarioRepository.delete(usuarioEntity);
    }

}