package com.iternova.ecommerce.service;

import com.iternova.ecommerce.entities.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    public Producto save(Producto producto);
    public Optional<Producto> get(Integer id); //Optional: nos da la posibilidad de validar si existe o no
    public void update(Producto producto);
    public  void delete (Integer id);
    public List<Producto> findAll();
}
