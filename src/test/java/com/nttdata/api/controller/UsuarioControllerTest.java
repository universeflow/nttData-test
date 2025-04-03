package com.nttdata.api.controller;

import com.nttdata.api.controllers.UsuarioController;
import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import com.nttdata.api.controllers.dtos.UsuarioResponseDto;
import com.nttdata.api.services.UsuarioService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

public class UsuarioControllerTest {


    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioRequestDto usuarioRequestDto;
    private UsuarioResponseDto usuarioResponseDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        usuarioRequestDto = UsuarioRequestDto.builder()
                .nombre("Juan test")
                .correo("juan.test@test.com")
                .password("S@eguro123e!")
                .build();

        usuarioResponseDto = UsuarioResponseDto.builder()
                .id(userId)
                .correo("juan.test@test.com")
                .nombre("Juan test")
                .build();
    }

    @Test
    void getAllUsers() {
        when(usuarioService.getAll()).thenReturn(Collections.singletonList(usuarioResponseDto));

        ResponseEntity<List<UsuarioResponseDto>> response = usuarioController.getAllUsers();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(usuarioResponseDto, response.getBody().get(0));

        verify(usuarioService, times(1)).getAll();
    }

    @Test
    void getUserById() {
        when(usuarioService.getUserById(userId)).thenReturn(Optional.of(usuarioResponseDto));

        ResponseEntity<UsuarioResponseDto> response = usuarioController.getUserById(userId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioResponseDto, response.getBody());

        verify(usuarioService, times(1)).getUserById(userId);
    }

    @Test
    void getUserByIdNotFound() {
        when(usuarioService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioResponseDto> response = usuarioController.getUserById(userId);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        verify(usuarioService, times(1)).getUserById(userId);
    }

    @Test
    void updateUser() {
        when(usuarioService.updateUsuario(userId, usuarioRequestDto)).thenReturn(usuarioResponseDto);

        ResponseEntity<UsuarioResponseDto> response = usuarioController.updateUser(userId, usuarioRequestDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioResponseDto, response.getBody());

        verify(usuarioService, times(1)).updateUsuario(userId, usuarioRequestDto);
    }

    @Test
    void patchUser() {
        when(usuarioService.patchUsuario(userId, usuarioRequestDto)).thenReturn(usuarioResponseDto);

        ResponseEntity<UsuarioResponseDto> response = usuarioController.patchUser(userId, usuarioRequestDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(usuarioResponseDto, response.getBody());

        verify(usuarioService, times(1)).patchUsuario(userId, usuarioRequestDto);
    }

    @Test
    void deleteUser() {
        doNothing().when(usuarioService).deleteUser(userId);

        ResponseEntity<Void> response = usuarioController.deleteUser(userId);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());

        verify(usuarioService, times(1)).deleteUser(userId);
    }
}

