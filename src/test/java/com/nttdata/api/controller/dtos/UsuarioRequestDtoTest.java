package com.nttdata.api.controller.dtos;

import com.nttdata.api.controllers.dtos.TelefonoDto;
import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsuarioRequestDtoTest {

    @Test
    void testUsuarioRequestDtoBuilder() {
        TelefonoDto telefonoDto = TelefonoDto.builder()
                .numero("123456789")
                .codigoPais("1")
                .codigoCiudad("57")
                .build();

        UsuarioRequestDto usuarioRequestDto = UsuarioRequestDto.builder()
                .correo("test@test.org")
                .password("SecurePass123!")
                .nombre("Leo Perez")
                .activo(true)
                .telefonos(Collections.singletonList(telefonoDto))
                .build();

        assertNotNull(usuarioRequestDto);
        assertEquals("test@test.org", usuarioRequestDto.getCorreo());
        assertEquals("SecurePass123!", usuarioRequestDto.getPassword());
        assertEquals("Leo Perez", usuarioRequestDto.getNombre());
        assertEquals(true, usuarioRequestDto.getActivo());

    }

    @Test
    void testUsuarioRequestDtoSettersAndGetters() {
        TelefonoDto telefonoDto = TelefonoDto.builder()
                .numero("123456789")
                .codigoPais("1")
                .codigoCiudad("57")
                .build();

        UsuarioRequestDto usuarioRequestDto =  UsuarioRequestDto.builder()
                .correo("test@test.org")
                .password("SecurePass123!")
                .nombre("Leo Perez")
                .activo(true)
                .telefonos(Collections.singletonList(telefonoDto))
                .build();

        assertNotNull(usuarioRequestDto);
        assertEquals("test@test.org", usuarioRequestDto.getCorreo());
        assertEquals("SecurePass123!", usuarioRequestDto.getPassword());
        assertEquals("Leo Perez", usuarioRequestDto.getNombre());
        assertEquals(telefonoDto, usuarioRequestDto.getTelefonos().get(0));
    }
}