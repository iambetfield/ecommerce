package com.iternova.ecommerce.service;

import com.iternova.ecommerce.entities.Orden;
import com.iternova.ecommerce.entities.Usuario;
import com.iternova.ecommerce.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenServiceImp implements OrdenService {
    @Autowired
    private OrdenRepository ordenRepository;

    @Override
    public List<Orden> findAll() {

        return ordenRepository.findAll();
    }

    @Override
    public Optional<Orden> findById(Integer id) {
        return ordenRepository.findById(id);

    }

    //creamos un método para generar el número de Orden
    public String generarNumeroOrden(){
        int num=0;
        String numeroConcatenado =""; // nos devuelve el string con el numero secuencial

        List<Orden> ordenes = findAll(); //traigo todas las listas existentes para saber luego la última
        List<Integer> numeros = new ArrayList<>(); // aca ponemos los números de orden, pasandolos a Integer

        ordenes.stream().forEach( o -> numeros.add(Integer.parseInt(o.getNumero()))); //agrego en mi lista de números el número de orden parseado a Integer
        if(ordenes.isEmpty()){
            num=1; //si esta vacia, arranca con el 1, sino ..
        }else{
            num= numeros.stream().max(Integer::compare).get(); //flujo para traer el máximo de la lista de números
            num++; // le sumamos uno
        }
        //ahora hay que pasar nuevamente el integer a cadena
        if(num<10){
            numeroConcatenado ="000000000" + String.valueOf(num); //pasamos a String el Integer
        } else if (num <100) {
            numeroConcatenado ="00000000" + String.valueOf(num);
        } else if (num <1000) {
            numeroConcatenado ="0000000" + String.valueOf(num);
        } else if (num <10000){
            numeroConcatenado ="000000" + String.valueOf(num);
        }
        return numeroConcatenado;
    }

    @Override
    public List<Orden> findByUsuario(Usuario usuario) {
        return ordenRepository.findByUsuario(usuario);
    }

    @Override
    public Orden save(Orden orden) {

        return ordenRepository.save(orden);
    }
}
