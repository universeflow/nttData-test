package com.nttdata.api.controller.dtos;

import com.nttdata.api.interfaces.ValidPassword;
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

    @Schema(description = "Password", example = "@Luis2025")
    @NotBlank
    @ValidPassword
    private String password;

    @Schema(description = "Ejemplo nombre", example = "Leo perez")
    private String nombre;

    @Schema(description = "Indica si el usuario esta activo", example = "false")
    private Boolean activo;

    @Schema(description = "Lista de telefonos", example = "[{\n" +
            "        \"numero\": 1234567,\n" +
            "        \"codigoCiudad\": \"57\",\n" +
            "        \"codigoPais\": \"1\"\n" +
            "    }\n" +
            "   ]")
    private List<TelefonoDto> telefonos;

}