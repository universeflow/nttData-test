package com.nttdata.api.controller.dtos;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UsuarioResponseDtoTest {

    @Test
    void testUsuarioResponseDtoBuilder() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        TelefonoDto telefonoDto = TelefonoDto.builder()
                .numero("123456789")
                .codigoPais("1")
                .codigoCiudad("57")
                .build();

        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.builder()
                .id(id)
                .nombre("Leo test")
                .correo("leo@test.org")
                .creado(now)
                .modificado(now)
                .ultimoLogin(now)
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .activo(true)
                .telefonos(Collections.singletonList(telefonoDto))
                .build();

        assertNotNull(usuarioResponseDto);
        assertEquals(id, usuarioResponseDto.getId());
        assertEquals("Leo test", usuarioResponseDto.getNombre());
        assertEquals("leo@test.org", usuarioResponseDto.getCorreo());
        assertEquals(now, usuarioResponseDto.getCreado());
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", usuarioResponseDto.getToken());
        assertEquals(1, usuarioResponseDto.getTelefonos().size());
        assertEquals(telefonoDto, usuarioResponseDto.getTelefonos().get(0));
    }

    @Test
    void testUsuarioResponseDtoSettersAndGetters() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        TelefonoDto telefonoDto = TelefonoDto.builder()
                .numero("123456789")
                .codigoPais("1")
                .codigoCiudad("57")
                .build();

        UsuarioResponseDto usuarioResponseDto =  UsuarioResponseDto.builder()
                .id(id)
                .nombre("Leo test")
                .correo("leo@test.org")
                .creado(now)
                .modificado(now)
                .ultimoLogin(now)
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .activo(true)
                .telefonos(Collections.singletonList(telefonoDto))
                .build();

        assertNotNull(usuarioResponseDto);
        assertEquals(id, usuarioResponseDto.getId());
        assertEquals("Leo test", usuarioResponseDto.getNombre());
        assertEquals("leo@test.org", usuarioResponseDto.getCorreo());
        assertEquals(now, usuarioResponseDto.getUltimoLogin());
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", usuarioResponseDto.getToken());

        assertEquals(telefonoDto, usuarioResponseDto.getTelefonos().get(0));
    }
}