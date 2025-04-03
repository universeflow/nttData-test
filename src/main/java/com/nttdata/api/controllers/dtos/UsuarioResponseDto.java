package com.nttdata.api.controllers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Schema(title = "UsuarioResponseDto", description = "Dto con información del usuario")
public class UsuarioResponseDto {
    @Schema(description = "ID del cliente", example = "12314-1234-123ded2")
    private UUID id;

    @Schema(description = "Nombre del usuario", example = "Juan Rodriguez")
    private String nombre;

    @Schema(description = "Correo electrónico del usuario", example = "juan@rodriguez.org")
    private String correo;

    @Schema(description = "Fecha de creación del usuario", example = "2023-01-01T12:00:00")
    private LocalDateTime creado;

    @Schema(description = "Fecha de última modificación del usuario", example = "2023-01-02T12:00:00")
    private LocalDateTime modificado;

    @Schema(description = "Fecha del último inicio de sesión del usuario", example = "2023-01-03T12:00:00")
    private LocalDateTime ultimoLogin;

    @Schema(description = "Token de autenticación del usuario", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Estado activo del usuario", example = "true")
    private boolean activo;

    @Schema(description = "Lista de teléfonos del usuario")
    private List<TelefonoDto> telefonos;


}