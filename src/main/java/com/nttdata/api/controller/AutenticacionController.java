package com.nttdata.api.controller;


import com.nttdata.api.controller.dtos.UsuarioRequestDto;
import com.nttdata.api.controller.dtos.UsuarioResponseDto;
import com.nttdata.api.service.AutenticacionService;
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
    public ResponseEntity<UsuarioResponseDto> signup(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
        return ResponseEntity.ok(autenticacionService.signup(usuarioRequestDto));
    }


    @PostMapping("/login")
    @Operation(summary = "Inicio de sesión", description = "Autentica un usuario y genera un token JWT.")
    public ResponseEntity<UsuarioResponseDto> login(@Valid @RequestBody UsuarioRequestDto loginUserDto) {
        UsuarioResponseDto authenticatedUser = autenticacionService.login(loginUserDto);
        return ResponseEntity.ok(authenticatedUser);
    }

}