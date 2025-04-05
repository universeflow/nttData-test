package com.nttdata.api.controllers;


import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import com.nttdata.api.controllers.dtos.UsuarioResponseDto;
import com.nttdata.api.services.AutenticacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios")
public class AutenticacionController {
    private final AutenticacionService autenticacionService;

    public AutenticacionController(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }


    @PostMapping("/signup")
    @Operation(summary = "Registro de usuario", description = "Registra un usuario en la plataforma.")
    public ResponseEntity<UsuarioResponseDto> registro(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
        UsuarioResponseDto registeredUser = autenticacionService.signup(usuarioRequestDto);
        return ResponseEntity.ok(registeredUser);
    }


    @PostMapping("/login")
    @Operation(summary = "Inicio de sesión", description = "Autentica un usuario y genera un token JWT.")
    public ResponseEntity<UsuarioResponseDto> autenticar(@Valid @RequestBody UsuarioRequestDto loginUserDto) {
        UsuarioResponseDto authenticatedUser = autenticacionService.autenticar(loginUserDto);
        return ResponseEntity.ok(authenticatedUser);
    }
}