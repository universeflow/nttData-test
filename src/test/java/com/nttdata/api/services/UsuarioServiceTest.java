package com.nttdata.api.services;

import com.nttdata.api.controllers.dtos.TelefonoDto;
import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import com.nttdata.api.controllers.dtos.UsuarioResponseDto;
import com.nttdata.api.entities.UsuarioEntity;
import com.nttdata.api.repositories.UsuarioRepository;
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
    private JwtService jwtService;

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
                .nombre("John Doe")
                .correo("john.doe@example.com")
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

        usuarioService = new UsuarioService(usuarioRepository, jwtService, passwordEncoder, entityManager);

    }



    @Test
    void RetornarListaVaciaCuandoNoExistenUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.enableFilter(anyString())).thenReturn(filter);
        List<UsuarioResponseDto> usuarios = usuarioService.getAll();

        assertNotNull(usuarios);
        assertTrue(usuarios.isEmpty());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void ObtenerTodosLosUsuarios() {
        UsuarioEntity otroUsuarioEntity = new UsuarioEntity();
        otroUsuarioEntity.setId(UUID.randomUUID());
        otroUsuarioEntity.setNombre("Otro Usuario");
        otroUsuarioEntity.setCorreo("otro@example.com");
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
    void ObtenerUsuarioPorId() {
        UUID id = usuarioEntity.getId();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEntity));

        Optional<UsuarioResponseDto> usuario = usuarioService.getUserById(id);

        assertTrue(usuario.isPresent());
        assertEquals(usuarioEntity.getCorreo(), usuario.get().getCorreo());

        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void ActualizarUsuarioExitosamente() {
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
    void ActualizarPasswordUsuario() {
        UUID id = usuarioEntity.getId();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        UsuarioResponseDto response = usuarioService.patchUsuario(id, usuarioRequestDto);

        assertNotNull(response);
        assertEquals(usuarioEntity.getCorreo(), response.getCorreo());

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void EliminarUsuario() {
        UUID id = usuarioEntity.getId();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEntity));

        usuarioService.deleteUser(id);

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).delete(usuarioEntity);
    }
}

