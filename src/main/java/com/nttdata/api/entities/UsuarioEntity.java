package com.nttdata.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "usuarios")
@SQLDelete(sql = "UPDATE usuarios SET activo = false WHERE id = ?")
@FilterDef(name = "activarFiltro", parameters = @ParamDef(name = "isActivo", type = Boolean.class))
@Filter(name = "activarFiltro", condition = "activo = :isActivo")
@Getter
@Setter
public class UsuarioEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, length = 100, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creado;

    private LocalDateTime modificado;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Column(name = "token", length = 500)
    private String token;

    @Column(nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "usuarioEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<TelefonoEntity> telefonoEntities = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public  UsuarioEntity() {}






}