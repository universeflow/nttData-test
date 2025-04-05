package com.nttdata.api.controller;

import com.nttdata.api.controller.dtos.UsuarioRequestDto;
import com.nttdata.api.controller.dtos.UsuarioResponseDto;
import com.nttdata.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<UsuarioResponseDto>> getAll() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @Operation( summary = "Busca un usuario", description = "Retorna un usuario por id")
    @GetMapping("/{uuid}")
    public ResponseEntity<UsuarioResponseDto> getUsuarioById(@PathVariable UUID uuid) {
        return usuarioService.getUsuarioById(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation( summary = "Actualiza un usuario", description = "Actualiza datos del usuario por id")
    @PutMapping("/{uuid}")
    public ResponseEntity<UsuarioResponseDto> updateUsuario(@PathVariable UUID uuid, @RequestBody UsuarioRequestDto userDetails) {
        return ResponseEntity.ok(usuarioService.updateUsuario(uuid, userDetails));
    }

    @Operation( summary = "Actualiza password", description = "Actualiza password  id del usuario")
    @PatchMapping("/{uuid}")
    public ResponseEntity<UsuarioResponseDto> patchUsuario(@PathVariable UUID uuid, @Valid @RequestBody UsuarioRequestDto request) {
        System.out.println("UUID: " + uuid);
        return ResponseEntity.ok(usuarioService.patchUsuario(uuid,request));
    }

    @Operation( summary = "Elimina un usuario", description = "Elimina un usuario por uuid")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable UUID uuid) {
        usuarioService.deleteUsuario(uuid);
        return ResponseEntity.noContent().build();

    }

}