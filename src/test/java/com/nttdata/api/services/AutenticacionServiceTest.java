package com.nttdata.api.services;

import com.nttdata.api.controllers.dtos.TelefonoDto;
import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import com.nttdata.api.controllers.dtos.UsuarioResponseDto;
import com.nttdata.api.entities.TelefonoEntity;
import com.nttdata.api.entities.UsuarioEntity;
import com.nttdata.api.exceptions.UserAlreadyExistsException;
import com.nttdata.api.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AutenticacionServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AutenticacionService autenticacionService;

    private UsuarioRequestDto usuarioRequestDto;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        usuarioRequestDto = UsuarioRequestDto.builder()
                .nombre("John Doe")
                .correo("john.doe@example.com")
                .password("SecurePass123!")
                .telefonos(List.of(TelefonoDto.builder()
                        .numero("123456789")
                        .codigoPais("1")
                        .codigoCiudad("57").build()))
                .build();

        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(UUID.randomUUID());
        usuarioEntity.setNombre(usuarioRequestDto.getNombre());
        usuarioEntity.setCorreo(usuarioRequestDto.getCorreo());
        usuarioEntity.setPassword("encodedPassword");
        usuarioEntity.setCreado(LocalDateTime.now());
        usuarioEntity.setModificado(LocalDateTime.now());
        usuarioEntity.setUltimoLogin(LocalDateTime.now());
        usuarioEntity.setActivo(true);
    }


    @Test
    void signupUsuarioExitosamente() {
        when(usuarioRepository.existsByCorreo(usuarioRequestDto.getCorreo())).thenReturn(false);
        when(passwordEncoder.encode(usuarioRequestDto.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(usuarioRequestDto.getCorreo())).thenReturn("mockToken");
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        UsuarioResponseDto response = autenticacionService.signup(usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioEntity.getCorreo(), response.getCorreo());
        assertEquals("mockToken", response.getToken());

        verify(usuarioRepository, times(1)).existsByCorreo(usuarioRequestDto.getCorreo());
        verify(passwordEncoder, times(1)).encode(usuarioRequestDto.getPassword());
        verify(jwtService, times(1)).generateToken(usuarioRequestDto.getCorreo());
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void signupUsuarioYaRegistrado() {
        when(usuarioRepository.existsByCorreo(usuarioRequestDto.getCorreo())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            autenticacionService.signup(usuarioRequestDto);
        });

        assertEquals("El correo ya está registrado", exception.getMessage());

        verify(usuarioRepository, times(1)).existsByCorreo(usuarioRequestDto.getCorreo());
        verify(passwordEncoder, never()).encode(anyString());
        verify(jwtService, never()).generateToken(anyString());
        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
    }

    @Test
    void signupUsuarioConTelefonos() {
        usuarioRequestDto.setTelefonos(List.of(TelefonoDto.builder()
                .numero("123456789")
                .codigoPais("1")
                .codigoCiudad("57").build()));

        when(usuarioRepository.existsByCorreo(usuarioRequestDto.getCorreo())).thenReturn(false);
        when(passwordEncoder.encode(usuarioRequestDto.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(usuarioRequestDto.getCorreo())).thenReturn("mockToken");
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        UsuarioResponseDto response = autenticacionService.signup(usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioEntity.getCorreo(), response.getCorreo());
        assertEquals("mockToken", response.getToken());

        verify(usuarioRepository, times(1)).existsByCorreo(usuarioRequestDto.getCorreo());
        verify(passwordEncoder, times(1)).encode(usuarioRequestDto.getPassword());
        verify(jwtService, times(1)).generateToken(usuarioRequestDto.getCorreo());
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void autenticarUsuarioExitosamente() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByCorreo(usuarioRequestDto.getCorreo()))
                .thenReturn(Optional.of(usuarioEntity));
        when(jwtService.generateToken(usuarioEntity.getCorreo()))
                .thenReturn("mockToken");
        when(usuarioRepository.save(any(UsuarioEntity.class)))
                .thenReturn(usuarioEntity);

        UsuarioResponseDto response = autenticacionService.autenticar(usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioEntity.getCorreo(), response.getCorreo());
        assertEquals("mockToken", response.getToken());


        verify(usuarioRepository, times(1))
                .save(usuarioEntity);
    }

    @Test
    void autenticarUsuarioConCredencialesIncorrectas() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciales incorrectas"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            autenticacionService.autenticar(usuarioRequestDto);
        });

        assertEquals("Credenciales incorrectas", exception.getMessage());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, never())
                .findByCorreo(anyString());
        verify(jwtService, never())
                .generateToken(anyString());
        verify(usuarioRepository, never())
                .save(any(UsuarioEntity.class));
    }

    @Test
    void autenticarUsuarioNoEncontrado() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(usuarioRepository.findByCorreo(usuarioRequestDto.getCorreo()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            autenticacionService.autenticar(usuarioRequestDto);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, times(1))
                .findByCorreo(usuarioRequestDto.getCorreo());
        verify(jwtService, never())
                .generateToken(anyString());
        verify(usuarioRepository, never())
                .save(any(UsuarioEntity.class));
    }
}

