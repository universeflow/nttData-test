package com.nttdata.api.controller;

import com.nttdata.api.controller.dtos.UsuarioRequestDto;
import com.nttdata.api.controller.dtos.UsuarioResponseDto;
import com.nttdata.api.service.AutenticacionService;
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

        ResponseEntity<UsuarioResponseDto> response = autenticacionController.signup(usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioResponseDto, response.getBody());

        verify(autenticacionService, times(1)).signup(usuarioRequestDto);
    }

    @Test
    void loginUsuarioExitosamente() {
        when(autenticacionService.login(usuarioRequestDto)).thenReturn(usuarioResponseDto);

        ResponseEntity<UsuarioResponseDto> response = autenticacionController.login(usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioResponseDto, response.getBody());

        verify(autenticacionService, times(1)).login(usuarioRequestDto);
    }

    @Test
    void loginUsuarioConCredencialesIncorrectas() {
        when(autenticacionService.login(usuarioRequestDto)).thenThrow(new BadCredentialsException("Credenciales incorrectas"));

        Exception exception = assertThrows(BadCredentialsException.class, () -> { autenticacionController.login(usuarioRequestDto);});

        assertEquals("Credenciales incorrectas", exception.getMessage());

        verify(autenticacionService, times(1)).login(usuarioRequestDto);
    }
}