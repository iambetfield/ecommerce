package com.iternova.ecommerce.controllers;

import com.iternova.ecommerce.entities.Orden;
import com.iternova.ecommerce.entities.Usuario;
import com.iternova.ecommerce.service.OrdenService;
import com.iternova.ecommerce.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/registrar") //usuario/registro
    public String registar(){
        return "usuario/registro";
    }

    //registro de usuarios
    @PostMapping("/registro")
    public String registro(Usuario usuario){
        usuario.setTipo("USER");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); //encripta la clave para el user
        usuarioService.save(usuario);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "usuario/login";
    }


    //inicio de SESION
    @GetMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session){
        Optional<Usuario> user = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));
        if(user.isPresent()){ //si el usuario está presente..
            session.setAttribute("idusuario", user.get().getId()); //1er parámetro nombre, 2°parámetro valor
            session.setAttribute("nombreUsuario", user.get().getNombre());
            if(user.get().getTipo().equals("ADMIN")){
                return "redirect:/admin"; //si está presente Y es ADMIN
            }
        }else {
            return "redirect:usuario/login"; //si no está presente
        }

        return "redirect:/"; //si se loguea te manda al home
    }


    @GetMapping("/compras")
    public String compras(HttpSession session, ModelMap modelo){
        modelo.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        List<Orden> ordenes = ordenService.findByUsuario(usuario);
        modelo.addAttribute("ordenes", ordenes);
        return "usuario/compras";
    }

    @GetMapping("/detalle/{id}")
    public String detalleCompra(@PathVariable Integer id, HttpSession session, ModelMap modelo){
        Optional<Orden> orden = ordenService.findById(id);
        modelo.addAttribute("detalles", orden.get().getDetalleOrden());

        //sesion
        modelo.addAttribute(session.getAttribute("idusuario"));
        return "usuario/detallecompra";
    }

    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session){
        session.removeAttribute("idusuario"); //remuevo la sesion

        return "redirect:/";
    }

}
