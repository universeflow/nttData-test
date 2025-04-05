package com.nttdata.api.repositories;

import com.nttdata.api.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
    @EntityGraph(attributePaths = "telefonoEntities")
    Optional<UsuarioEntity> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

}
