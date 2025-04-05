package com.nttdata.api.controller;

import com.nttdata.api.controller.dtos.UsuarioRequestDto;
import com.nttdata.api.controller.dtos.UsuarioResponseDto;
import com.nttdata.api.service.UsuarioService;
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

        ResponseEntity<List<UsuarioResponseDto>> response = usuarioController.getAll();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals(usuarioResponseDto, response.getBody().get(0));

        verify(usuarioService, times(1)).getAll();
    }

    @Test
    void getUsuarioById() {
        when(usuarioService.getUsuarioById(userId)).thenReturn(Optional.of(usuarioResponseDto));

        ResponseEntity<UsuarioResponseDto> response = usuarioController.getUsuarioById(userId);

        assertNotNull(response);
        assertEquals(usuarioResponseDto, response.getBody());

        verify(usuarioService, times(1)).getUsuarioById(userId);
    }

    @Test
    void getUsuarioByIdNotFound() {
        when(usuarioService.getUsuarioById(userId)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioResponseDto> response = usuarioController.getUsuarioById(userId);

        assertNotNull(response);

        verify(usuarioService, times(1)).getUsuarioById(userId);
    }

    @Test
    void updateUser() {
        when(usuarioService.updateUsuario(userId, usuarioRequestDto)).thenReturn(usuarioResponseDto);

        ResponseEntity<UsuarioResponseDto> response = usuarioController.updateUsuario(userId, usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioResponseDto, response.getBody());

        verify(usuarioService, times(1)).updateUsuario(userId, usuarioRequestDto);
    }

    @Test
    void patchUser() {
        UUID uuid = UUID.randomUUID();
        when(usuarioService.patchUsuario(uuid, usuarioRequestDto)).thenReturn(usuarioResponseDto);

        when(usuarioService.patchUsuario(uuid,usuarioRequestDto)).thenReturn(usuarioResponseDto);

        ResponseEntity<UsuarioResponseDto> response = usuarioController.patchUsuario(uuid,usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioResponseDto, response.getBody());

        verify(usuarioService, times(1)).patchUsuario(uuid,usuarioRequestDto);
    }

    @Test
    void deleteUser() {
        doNothing().when(usuarioService).deleteUsuario(userId);

        ResponseEntity<Void> response = usuarioController.deleteUsuario(userId);

        assertNotNull(response);

        verify(usuarioService, times(1)).deleteUsuario(userId);
    }
}

