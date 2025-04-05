package com.nttdata.api.service;

import com.nttdata.api.controller.dtos.TelefonoDto;
import com.nttdata.api.controller.dtos.UsuarioRequestDto;
import com.nttdata.api.controller.dtos.UsuarioResponseDto;
import com.nttdata.api.entity.UsuarioEntity;
import com.nttdata.api.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @Mock
    private Filter filter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioRequestDto usuarioRequestDto;
    private UsuarioEntity usuarioEntity;



    @BeforeEach
    void setUp() {
        usuarioRequestDto = UsuarioRequestDto.builder()
                .nombre("leo test")
                .correo("leo@test.com")
                .activo(true)
                .telefonos(List.of(TelefonoDto.builder()
                        .numero("123456789")
                        .codigoPais("1")
                        .codigoCiudad("57").build()))
                .build();

        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(UUID.randomUUID());
        usuarioEntity.setNombre(usuarioRequestDto.getNombre());
        usuarioEntity.setCorreo(usuarioRequestDto.getCorreo());
        usuarioEntity.setPassword("encodedPassword");
        usuarioEntity.setCreado(LocalDateTime.now());
        usuarioEntity.setModificado(LocalDateTime.now());
        usuarioEntity.setUltimoLogin(LocalDateTime.now());
        usuarioEntity.setActivo(true);

        usuarioService = new UsuarioService(usuarioRepository,  passwordEncoder, entityManager);

    }



    @Test
    void retornaListaVaciaCuandoNoExistenUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter(anyString())).thenReturn(filter);
        List<UsuarioResponseDto> usuarios = usuarioService.getAll();

        assertNotNull(usuarios);
        assertTrue(usuarios.isEmpty());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void obteneTodosLosUsuarios() {
        UsuarioEntity otroUsuarioEntity = new UsuarioEntity();
        otroUsuarioEntity.setId(UUID.randomUUID());
        otroUsuarioEntity.setNombre("Otro Usuario");
        otroUsuarioEntity.setCorreo("otro@test.com");
        otroUsuarioEntity.setTelefonoEntities(Collections.emptyList());
        otroUsuarioEntity.setCreado(LocalDateTime.now());
        otroUsuarioEntity.setModificado(LocalDateTime.now());
        otroUsuarioEntity.setUltimoLogin(LocalDateTime.now());
        otroUsuarioEntity.setActivo(true);

        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity, otroUsuarioEntity));


        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter(anyString())).thenReturn(filter);
        List<UsuarioResponseDto> usuarios = usuarioService.getAll();

        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void obteneUsuarioPorId() {
        UUID id = usuarioEntity.getId();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEntity));

        Optional<UsuarioResponseDto> usuario = usuarioService.getUsuarioById(id);

        assertTrue(usuario.isPresent());
        assertEquals(usuarioEntity.getCorreo(), usuario.get().getCorreo());

        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void actualizaUsuarioExitosamente() {
        UUID id = usuarioEntity.getId();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        UsuarioResponseDto response = usuarioService.updateUsuario(id, usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioRequestDto.getCorreo(), response.getCorreo());
        assertEquals(usuarioRequestDto.getNombre(), response.getNombre());

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void patchPasswordUsuarioExitosamente() {
        UUID uuid = usuarioEntity.getId();

        when(usuarioRepository.findById(uuid)).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.encode(usuarioRequestDto.getPassword())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        UsuarioResponseDto response = usuarioService.patchUsuario(uuid, usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioRequestDto.getCorreo(), response.getCorreo());
        assertEquals(usuarioEntity.getNombre(), response.getNombre());

        verify(usuarioRepository, times(1)).findById(uuid);
        verify(passwordEncoder, times(1)).encode(usuarioRequestDto.getPassword());
        verify(usuarioRepository, times(1)).save(usuarioEntity);
    }

    @Test
    void patchPasswordUsuarioNoEncontrado() {
        UUID id = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> usuarioService.patchUsuario(id,usuarioRequestDto));

        verify(passwordEncoder, times(0)).encode(anyString());
        verify(usuarioRepository, times(0)).save(any(UsuarioEntity.class));
    }

    @Test
    void eliminaUsuario() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEntity));

        usuarioService.deleteUsuario(id);

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).delete(usuarioEntity);
    }
}

