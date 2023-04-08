package com.iternova.ecommerce.repository;

import com.iternova.ecommerce.entities.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {
}
