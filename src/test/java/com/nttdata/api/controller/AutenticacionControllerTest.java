package com.nttdata.api.controller;

import com.nttdata.api.controllers.AutenticacionController;
import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import com.nttdata.api.controllers.dtos.UsuarioResponseDto;
import com.nttdata.api.services.AutenticacionService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

public class AutenticacionControllerTest {
    @Mock
    private AutenticacionService autenticacionService;

    @InjectMocks
    private AutenticacionController autenticacionController;

    private UsuarioRequestDto usuarioRequestDto;
    private UsuarioResponseDto usuarioResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuarioRequestDto = UsuarioRequestDto.builder()
                .nombre("John Doe")
                .correo("john.doe@example.com")
                .password("SecurePass123!")
                .build();

        usuarioResponseDto = UsuarioResponseDto.builder()
                .correo("john.doe@example.com")
                .nombre("John Doe")
                .token("mockToken")
                .build();
    }

    @Test
    void signupUsuarioExitosamente() {
        when(autenticacionService.signup(usuarioRequestDto)).thenReturn(usuarioResponseDto);

        ResponseEntity<UsuarioResponseDto> response = autenticacionController.registro(usuarioRequestDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioResponseDto, response.getBody());

        verify(autenticacionService, times(1)).signup(usuarioRequestDto);
    }

    @Test
    void loginUsuarioExitosamente() {
        when(autenticacionService.autenticar(usuarioRequestDto)).thenReturn(usuarioResponseDto);

        ResponseEntity<UsuarioResponseDto> response = autenticacionController.autenticar(usuarioRequestDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioResponseDto, response.getBody());

        verify(autenticacionService, times(1)).autenticar(usuarioRequestDto);
    }

    @Test
    void loginUsuarioConCredencialesIncorrectas() {
        when(autenticacionService.autenticar(usuarioRequestDto)).thenThrow(new BadCredentialsException("Credenciales incorrectas"));

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            autenticacionController.autenticar(usuarioRequestDto);
        });

        assertEquals("Credenciales incorrectas", exception.getMessage());

        verify(autenticacionService, times(1)).autenticar(usuarioRequestDto);
    }
}