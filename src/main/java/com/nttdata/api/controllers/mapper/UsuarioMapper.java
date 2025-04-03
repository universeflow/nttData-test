package com.nttdata.api.controllers.mapper;


import com.nttdata.api.controllers.dtos.TelefonoDto;
import com.nttdata.api.controllers.dtos.UsuarioRequestDto;
import com.nttdata.api.controllers.dtos.UsuarioResponseDto;
import com.nttdata.api.entities.TelefonoEntity;
import com.nttdata.api.entities.UsuarioEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsuarioMapper {

    public  static TelefonoEntity mapToTelefonoEntity(TelefonoDto telefono,UsuarioEntity usuarioEntity) {

        TelefonoEntity telefonoEntity = new TelefonoEntity();
        telefonoEntity.setNumero(telefono.getNumero());
        telefonoEntity.setCodigoCiudad(telefono.getCodigoCiudad());
        telefonoEntity.setCodigoPais(telefono.getCodigoPais());
        telefonoEntity.setUsuarioEntity(usuarioEntity);

        return telefonoEntity;

    }


    public  static TelefonoDto  mapToTelefonoDto(TelefonoEntity entity) {

        return TelefonoDto.builder()
                .codigoCiudad(entity.getCodigoCiudad())
                .numero(entity.getNumero())
                .codigoPais(entity.getCodigoPais())
                .build();

    }


    public  static UsuarioEntity mapToUsuarioEntity(UsuarioRequestDto dto,PasswordEncoder passwordEncoder) {
        LocalDateTime now = LocalDateTime.now();
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(UUID.randomUUID());
        usuarioEntity.setNombre(dto.getNombre());
        usuarioEntity.setCorreo(dto.getCorreo());
        usuarioEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuarioEntity.setCreado(now);
        usuarioEntity.setModificado(now);
        usuarioEntity.setUltimoLogin(now);

        return usuarioEntity;

    }


    public  static UsuarioResponseDto mapEntityToUsuarioResponseDto(UsuarioEntity usuarioEntity, List<TelefonoDto> telefonoDtos){

        return UsuarioResponseDto.builder()
                .id(usuarioEntity.getId())
                .creado(usuarioEntity.getCreado())
                .ultimoLogin(usuarioEntity.getUltimoLogin())
                .token(usuarioEntity.getToken())
                .modificado(usuarioEntity.getModificado())
                .activo(usuarioEntity.isActivo())
                .correo(usuarioEntity.getCorreo())
                .nombre(usuarioEntity.getNombre())
                .telefonos(telefonoDtos)
                .build();

    }

}
