package com.nttdata.api.controllers;

import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import com.nttdata.api.controllers.dtos.UsuarioResponseDto;
import com.nttdata.api.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
@Tag(name = "Usuarios", description = "Endpoints para la gestión de usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtener usuarios", description = "Retorna la lista de usuarios registrados en la plataforma.")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAllUsers() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getUserById(@PathVariable UUID id) {
        return usuarioService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> updateUser(@PathVariable UUID id, @RequestBody UsuarioRequestDto userDetails) {
        return ResponseEntity.ok(usuarioService.updateUsuario(id, userDetails));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> patchUser(@PathVariable UUID id, @RequestBody UsuarioRequestDto updates) {
        return ResponseEntity.ok(usuarioService.patchUsuario(id, updates));
    }

    @Operation( summary = "Eliminación de usuarios", description = "Elimina un usuario por id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        usuarioService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }

}