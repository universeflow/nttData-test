package com.nttdata.api.services;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;
    private String secretKey;

    @BeforeEach
    void setUp() {
        userDetails = new User("testuser", "password", Collections.emptyList());

        secretKey = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());

        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L); // 1 hora
    }

    @Test
    void generaTokenValido() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void generaTokenConClaimsExtras() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        String token = jwtService.generateToken(claims, userDetails);
        assertNotNull(token);
    }

    @Test
    void extraeNombreDeUsuarioDelToken() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void lanzaExcepcionParaTokenMalformado() {
        String tokenMalformado = "formato.token.invalido";
        assertThrows(MalformedJwtException.class, () -> jwtService.extractUsername(tokenMalformado));
    }

    @Test
    void validaTokenCorrectamente() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void invalidaTokenParaUsuarioDiferente() {
        String token = jwtService.generateToken(userDetails);
        UserDetails usuarioDiferente = new User("otroUsuario", "password", Collections.emptyList());
        assertFalse(jwtService.isTokenValid(token, usuarioDiferente));
    }

    @Test
    void retornaTiempoDeExpiracionCorrecto() {
        assertEquals(3600000L, jwtService.getExpirationTime());
    }
}

