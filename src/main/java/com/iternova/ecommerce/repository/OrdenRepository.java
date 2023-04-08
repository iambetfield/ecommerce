package com.iternova.ecommerce.repository;

import com.iternova.ecommerce.entities.Orden;
import com.iternova.ecommerce.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository< Orden, Integer> {

  List<Orden> findByUsuario(Usuario usuario);
}
