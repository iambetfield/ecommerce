package com.iternova.ecommerce.repository;

import com.iternova.ecommerce.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //obtenemos un usuario x email - por defecto se busca por id-

    Optional<Usuario> findByEmail(String email);
}
