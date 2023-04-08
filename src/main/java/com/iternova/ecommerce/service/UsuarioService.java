package com.iternova.ecommerce.service;

import com.iternova.ecommerce.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> findById(Integer id);

    Usuario save(Usuario usuario);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAll();

}
