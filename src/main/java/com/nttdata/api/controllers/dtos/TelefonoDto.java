package com.nttdata.api.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Objeto Telefono")
public class TelefonoDto {

    @Schema(description = "ID de telefono ", example = "7777-9999-123ded2")
    private Long id;
    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9]{6,15}$", message = "El número de teléfono debe contener entre 6 y 15 dígitos")
    @Schema(description = "Numero telefonico", example = "999313922")
    private String numero;

    @NotBlank(message = "El código de ciudad no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El código de ciudad debe ser numérico")
    @Schema(description = "Codigo Ciudad", example = "02")
    private String codigoCiudad;

    @NotBlank(message = "El código de país no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El código de país debe ser numérico")
    @Schema(description = "Codigo pais", example = "59")
    private String codigoPais;


}
