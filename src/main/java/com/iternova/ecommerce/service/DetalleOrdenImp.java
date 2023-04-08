package com.iternova.ecommerce.service;

import com.iternova.ecommerce.entities.DetalleOrden;
import com.iternova.ecommerce.repository.DetalleOrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleOrdenImp implements DetalleOrdenService{
    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;
    @Override
    public DetalleOrden save(DetalleOrden detalleOrden) {
       return detalleOrdenRepository.save(detalleOrden);
    }
}
