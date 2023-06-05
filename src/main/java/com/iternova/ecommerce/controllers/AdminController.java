package com.iternova.ecommerce.controllers;

import com.iternova.ecommerce.entities.Orden;
import com.iternova.ecommerce.entities.Producto;
import com.iternova.ecommerce.service.OrdenService;
import com.iternova.ecommerce.service.ProductoService;
import com.iternova.ecommerce.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private OrdenService ordenService;
    @GetMapping("")
    public String home(ModelMap modelo, HttpSession session) {

        List<Producto> productos = productoService.findAll();
        modelo.addAttribute("productos", productos);


        modelo.addAttribute("sesion", session.getAttribute("idusuario"));

        //obtener el nombre de usuario
        String username= (String) session.getAttribute("username");
        modelo.addAttribute("username", username);

        return "/admin/home"; // no hace falta poner la extensión -- siempre 1° busca en carpeta template
    }
    @GetMapping("/usuarios")
    public String usuarios(ModelMap modelo){
        modelo.addAttribute("usuarios", usuarioService.findAll());
        return "admin/usuarios";
    }

    @GetMapping("/ordenes")
    public String ordenes(ModelMap modelo){
        modelo.addAttribute("ordenes", ordenService.findAll());
        return "/admin/ordenes";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Integer id, ModelMap modelo){
        Orden orden = ordenService.findById(id).get();
        modelo.addAttribute("detalles", orden.getDetalleOrden());
        return "admin/detalleorden";

    }

}
