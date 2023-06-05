package com.iternova.ecommerce.controllers;

import com.iternova.ecommerce.entities.DetalleOrden;
import com.iternova.ecommerce.entities.Orden;
import com.iternova.ecommerce.entities.Producto;
import com.iternova.ecommerce.entities.Usuario;
import com.iternova.ecommerce.service.DetalleOrdenService;
import com.iternova.ecommerce.service.OrdenService;
import com.iternova.ecommerce.service.ProductoService;
import com.iternova.ecommerce.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private OrdenService ordenService;
    @Autowired
    private DetalleOrdenService detalleOrdenService;

    //creamos una lista de detalles para almacenar los detalles de la orden
    List<DetalleOrden> detalles = new ArrayList();

    //almacena los datos de la orden
    Orden orden = new Orden();

    @GetMapping("/")
    public String home(ModelMap modelo, HttpSession session){
        modelo.addAttribute("productos", productoService.findAll());
        modelo.addAttribute("sesion", session.getAttribute("idusuario"));

        //obtener el nombre de usuario
        String username= (String) session.getAttribute("username");
        modelo.addAttribute("username", username);
        return "usuario/home";
    }
    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, ModelMap modelo){
        Producto producto = new Producto();
        Optional<Producto> respuesta = productoService.get(id);
        producto = respuesta.get();
        modelo.addAttribute("producto", producto);
        return "usuario/productohome";
    }


    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, ModelMap modelo){
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal= 0;

        Optional<Producto> respuesta = productoService.get(id);
        producto = respuesta.get();
        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio()); //?
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio()*cantidad);
        detalleOrden.setProducto(producto);

        //validar que el producto no se añada 2 veces
        Integer idProducto = producto.getId();
        boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

        if(!ingresado){
            detalles.add(detalleOrden);
        }




        sumaTotal = detalles.stream().mapToDouble(dt-> dt.getTotal()).sum();
        orden.setTotal(sumaTotal);
        modelo.addAttribute("cart", detalles);
        modelo.addAttribute("orden", orden);

        return "usuario/carrito";
    }

    //quitar un producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductCart(@PathVariable Integer id, ModelMap modelo){

        //lista nueva de productos
        List<DetalleOrden> ordenesNuevas = new ArrayList<>();

        for (DetalleOrden detalleOrden : detalles){
            if(detalleOrden.getProducto().getId()!=id){
                ordenesNuevas.add(detalleOrden);
            }
        }

        //poner la nueva lista con los productos restantes
        detalles = ordenesNuevas;

        double sumaTotal=0;
        sumaTotal = detalles.stream().mapToDouble(dt-> dt.getTotal()).sum();
        orden.setTotal(sumaTotal);
        modelo.addAttribute("cart", detalles);
        modelo.addAttribute("orden", orden);


        return "usuario/carrito";
    }

    @GetMapping("/getCart")
    public String getCart(ModelMap modelo, HttpSession session){
        modelo.addAttribute("cart", detalles); //porque detalles y orden son atributos globales
        modelo.addAttribute("orden", orden);

        //sesion
        modelo.addAttribute("sesion", session.getAttribute("idusuario"));
        return "/usuario/carrito";
    }
    //mostrar la orden
    @GetMapping("/order")
    public String order(ModelMap modelo, HttpSession session){
                                                //pasamos a integer el objeto, que previamente tuvo que pasarse a String
        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get(); // por ahora lo hardcodeo con el admin

        modelo.addAttribute("cart", detalles);
        modelo.addAttribute("orden", orden);
        modelo.addAttribute("usuario", usuario);
        return "usuario/resumenorden";
    }

    //guardar la orden
    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session){
        Date fechaCreacion = new Date();
        orden.setFechaCreacion(fechaCreacion); // establecemos la fecha de creacion

        orden.setNumero(ordenService.generarNumeroOrden()); //creamos numero de orden

        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get(); // obtenemos el usuario
        orden.setUsuario(usuario); //seteamos ese usaurio

        ordenService.save(orden); //guardamos

        // guardar detalles
        for (DetalleOrden dt:detalles){
            dt.setOrden(orden);
            detalleOrdenService.save(dt); //guardamos tanto la orden como el detalle
        }
        //limpiar detalles de la orden por si quiere seguir comprando
        orden = new Orden();
        detalles.clear();


        return "redirect:/";
    }

    //barra de búsqueda

    @PostMapping("/busqueda")
    public String busqueda(@RequestParam String nombre, ModelMap modelo){

        String nombreBusqueda = nombre.toLowerCase();

        if(nombre.isEmpty()){
            return "redirect:/";
        }
                                                            //flujo->filtra x el nombre que contenga "nombre" -> pasamos el string a una lista
        List<Producto> productos = productoService.findAll().stream().filter(p -> p.getNombre().toLowerCase().contains(nombreBusqueda)).collect(Collectors.toList());

        modelo.addAttribute("productos", productos);

        return "usuario/home";
    }


}
