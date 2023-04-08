package com.iternova.ecommerce.service;

import com.iternova.ecommerce.entities.Orden;
import com.iternova.ecommerce.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface OrdenService {

    List<Orden> findAll();
    Optional<Orden> findById(Integer id);
    Orden save(Orden orden);

    String generarNumeroOrden();

    List<Orden> findByUsuario(Usuario usuario);
}
