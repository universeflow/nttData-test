package com.nttdata.api.controllers.dtos;

import com.nttdata.api.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Objeto de usuario request")
public class UsuarioRequestDto {

    @NotBlank
    @Email(message = "El formato del correo es incorrecto")
    @Schema(description = "Ejemplo correo", example = "test@test.org")
    private String correo;

    @NotBlank
    @ValidPassword
    private String password;

    @Schema(description = "Ejemplo nombre", example = "Leo perez")
    private String nombre;

    @Schema(description = "Indica si el usuario esta activo", example = "false")
    private Boolean activo;

    @Schema(description = "Lista de telefonos", example = "telefonos { numero : 12313123, codigoCiudad : 123, codigoPais : 123 }")
    private List<TelefonoDto> telefonos;

}