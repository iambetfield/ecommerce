package com.iternova.ecommerce.service;

import com.iternova.ecommerce.entities.Producto;
import com.iternova.ecommerce.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImp implements ProductoService{
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Producto save(Producto producto) {

        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> get(Integer id) {

        return productoRepository.findById(id);
    }

    @Override
    public void update(Producto producto) {
        productoRepository.save(producto); // si es null, lo crea, si ya existe, lo actualiza
    }

    @Override
    public void delete(Integer id) {

        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }
}
