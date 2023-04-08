package com.iternova.ecommerce.controllers;

import com.iternova.ecommerce.entities.Producto;
import com.iternova.ecommerce.entities.Usuario;
import com.iternova.ecommerce.service.ProductoService;
import com.iternova.ecommerce.service.UploadFileService;
import com.iternova.ecommerce.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private UploadFileService upload;

    @Autowired
    private UsuarioService usuarioService;

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
    @GetMapping("")
    public String show(ModelMap modelo){
        modelo.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create(){
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException { //lo toma del name="img" en create.html
        LOGGER.info("este es el objeto producto{}", producto);

        Usuario user = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get(); //el get es porque es un Optional

        producto.setUsuario(user);

        //logica para subir la imagen al servidor y guardar el nombre en la BD
        //validación para cargar un producto por primera vez
        if (producto.getId()==null){ //validación para cuando se crea un producto(el id aun es null)
            String nombreImagen= upload.saveImage(file); // metodo de la clase UploadFileService
            producto.setImagen(nombreImagen);
        }else{ //cuando se modifique el producto se cargue la misma imagen


        }

        productoService.save(producto);
        return "redirect:/productos";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, ModelMap modelo){
        Producto producto = new Producto();
        Optional<Producto> respuesta = productoService.get(id);
        if(respuesta.isPresent()){
            producto = respuesta.get();
        }
        modelo.addAttribute("producto", producto);
        return "productos/edit";
    }
    @PostMapping("/update")
    public String update(Producto producto,@RequestParam("img") MultipartFile file) throws IOException {

        Producto p = new Producto();
        p = productoService.get(producto.getId()).get();

        if(file.isEmpty()){

            producto.setImagen(p.getImagen());
        }else{ //cuando queramos cambiar también la imagen cuando editamos el producto


            //eliminar cuando no sea la imagen por defecto
            if(!p.getImagen().equals("default.jpg")){
                upload.deleteImage(p.getImagen());
            }

            String nombreImagen= upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){

        //eliminar la imagen del servidor
        Producto p = new Producto();
        p = productoService.get(id).get();
        //eliminar cuando NO sea la imagen por defecto, para que siempre quede la imagen gris
        if(!p.getImagen().equals("default.jpg")){
            upload.deleteImage(p.getImagen());
        }

        productoService.delete(id);
        return "redirect:/productos";
    }

}
