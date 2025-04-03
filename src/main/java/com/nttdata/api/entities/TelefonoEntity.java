package com.nttdata.api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "telefono")
@Getter
@Setter
public class TelefonoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false, name = "codigo_ciudad")
    private String codigoCiudad;

    @Column(nullable = false,name = "codigo_pais")
    private String codigoPais;

    @ManyToOne
    @JoinColumn(name = "usuarioEntity_id", nullable = false)
    private UsuarioEntity usuarioEntity;


    public TelefonoEntity(String codigoCiudad, String numero, String codigoPais, UsuarioEntity usuario) {
        this.codigoCiudad = codigoCiudad;
        this.numero = numero;
        this.codigoPais = codigoPais;
        this.usuarioEntity = usuario;
    }

    public TelefonoEntity(Long id,String codigoCiudad, String numero, String codigoPais, UsuarioEntity usuario) {
        this.id = id;
        this.codigoCiudad = codigoCiudad;
        this.numero = numero;
        this.codigoPais = codigoPais;
        this.usuarioEntity = usuario;
    }


    public TelefonoEntity() {
    }

}