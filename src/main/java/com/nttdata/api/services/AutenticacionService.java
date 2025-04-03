package com.nttdata.api.services;

import com.nttdata.api.controllers.mapper.UsuarioMapper;
import com.nttdata.api.controllers.dtos.TelefonoDto;
import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import com.nttdata.api.controllers.dtos.UsuarioResponseDto;
import com.nttdata.api.entities.TelefonoEntity;
import com.nttdata.api.entities.UsuarioEntity;
import com.nttdata.api.exceptions.UserAlreadyExistsException;
import com.nttdata.api.repositories.UsuarioRepository;
import java.util.ArrayList;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AutenticacionService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AutenticacionService(
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userRequest Datos del usuario a registrar
     * @return UsuarioResponseDto con la informacion del usuario creado
     */
    @Transactional
    public UsuarioResponseDto signup(UsuarioRequestDto userRequest) {
        log.info("Registrando nuevo usuario: {}", userRequest.getCorreo());

        if (usuarioRepository.existsByCorreo(userRequest.getCorreo())) {
            throw new UserAlreadyExistsException("El correo ya está registrado");
        }

        LocalDateTime now = LocalDateTime.now();


        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNombre(userRequest.getNombre());
        usuarioEntity.setCorreo(userRequest.getCorreo());
        usuarioEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        usuarioEntity.setCreado(now);
        usuarioEntity.setActivo(true);


        if (Objects.nonNull(userRequest.getTelefonos())) {
            List<TelefonoEntity> telefonoEntities = new ArrayList<>();
            userRequest.getTelefonos().forEach(telefono -> {
                telefonoEntities.add(UsuarioMapper.mapToTelefonoEntity(telefono, usuarioEntity));
            });
            usuarioEntity.setTelefonoEntities(telefonoEntities);
        }

        String token = jwtService.generateToken(userRequest.getCorreo());
        usuarioEntity.setToken(token);
        UsuarioEntity savedUsuarioEntity = usuarioRepository.save(usuarioEntity);



        List<TelefonoDto> telefonos = savedUsuarioEntity.getTelefonoEntities().stream()
                .map(UsuarioMapper::mapToTelefonoDto)
                                .toList();

        log.info("Usuario registrado: {}", savedUsuarioEntity.getNombre());
        return  UsuarioResponseDto.builder()
                .id(savedUsuarioEntity.getId())
                .ultimoLogin(savedUsuarioEntity.getUltimoLogin())
                .creado(savedUsuarioEntity.getCreado())
                .modificado(savedUsuarioEntity.getModificado())
                .activo(savedUsuarioEntity.isActivo())
                .token(token)
                .correo(savedUsuarioEntity.getCorreo())
                .nombre(savedUsuarioEntity.getNombre())
                .telefonos(telefonos)
                .build();


    }

    public UsuarioResponseDto autenticar(UsuarioRequestDto requestDto) {
        log.info("Autenticando usuario: {}", requestDto.getCorreo());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getCorreo(),
                            requestDto.getPassword()
                    )
            );

            UsuarioEntity usuarioEntity = usuarioRepository.findByCorreo(requestDto.getCorreo())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            usuarioEntity.setUltimoLogin(LocalDateTime.now());
            String token = jwtService.generateToken(usuarioEntity.getCorreo());
            usuarioEntity.setToken(token);
            usuarioRepository.save(usuarioEntity);

            List<TelefonoDto> telefonos = usuarioEntity.getTelefonoEntities().stream()
                    .map(phone ->  TelefonoDto.builder()
                            .numero( phone.getNumero())
                            .codigoCiudad(phone.getCodigoCiudad())
                            .codigoPais(phone.getCodigoPais())
                            .build())
                    .toList();

            log.info("Usuario autenticado: {}", usuarioEntity.getNombre());
            return  UsuarioResponseDto.builder()
                            .id(usuarioEntity.getId())
                            .creado(usuarioEntity.getCreado())
                            .token(token)
                            .ultimoLogin(usuarioEntity.getUltimoLogin())
                            .modificado(usuarioEntity.getModificado())
                            .activo(usuarioEntity.isActivo())
                            .correo(usuarioEntity.getCorreo())
                            .nombre(usuarioEntity.getNombre())
                            .telefonos(telefonos)
                            .build();


        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }
    }
}


