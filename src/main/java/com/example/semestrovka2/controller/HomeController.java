package com.example.semestrovka2.controller;

import com.example.semestrovka2.model.DetalleOrden;
import com.example.semestrovka2.model.Orden;
import com.example.semestrovka2.model.Producto;
import com.example.semestrovka2.model.Usuario;
import com.example.semestrovka2.service.DetalleOrdenService;
import com.example.semestrovka2.service.IUsuarioService;
import com.example.semestrovka2.service.OrdenService;
import com.example.semestrovka2.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")

public class HomeController {
    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ProductoService productoService;
    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private OrdenService ordenService;
    @Autowired
    private DetalleOrdenService detalleOrdenService;
    // almacena los detalles de la orden en el carrito
    List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
    // guarda los datos de la orden
    Orden orden = new Orden();

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("productos", productoService.findAll());
        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model) {
        log.info("la id se envia como parametro {}", id);
        Producto producto = new Producto();
        Optional<Producto> productoOptional = productoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);
        return "usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = productoService.get(id);
        log.info("Producto añadido: {}", optionalProducto.get());
        log.info("Cantidad: {}", cantidad);
        producto = optionalProducto.get();

        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);

        //sirve para verificar que el producto no se duplique mas de 2 veces
        Integer idProducto = producto.getId();
        //anyMatch es como un for que rastrea  toda la lista
        boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
        // si no es verdadero se anade a la lista
        if (!ingresado) {
            detalles.add(detalleOrden);
        }

        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "usuario/carrito";
    }

    // quitar un producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Integer id, Model model) {

        // lista nueva de prodcutos
        List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

        for (DetalleOrden detalleOrden : detalles) {
            if (detalleOrden.getProducto().getId() != id) {
                ordenesNueva.add(detalleOrden);
            }
        }

        // poner la nueva lista con los productos restantes
        detalles = ordenesNueva;

        double sumaTotal = 0;
        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "usuario/carrito";
    }
    @GetMapping("/getCart")
    public String getCart(Model model){
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        return "usuario/carrito";
    }
    @GetMapping("/order")
    public String order(Model model){

        Usuario usuario = usuarioService.findById(1).get();
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        model.addAttribute("usuario", usuario);
        return "usuario/resumenorden";
    }
    @GetMapping("/saveOrder")
    public String saveOrder(){
        Date fechaCreacion = new Date();
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(ordenService.generarNumeroOrden());
        // user
        Usuario usuario = usuarioService.findById(1).get();
        orden.setUsuario(usuario);
        ordenService.save(orden);
        //guardar detalles
        for (DetalleOrden dt: detalles){
            dt.setOrden(orden);
            detalleOrdenService.save(dt);
        }
        //limpiarvalores lista e orden
        orden = new Orden();
        detalles.clear();

        return "redirect:/";
    }
    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombre, Model model){
        log.info("nombre del produco{}",nombre);
        List<Producto> productos = productoService.findAll().stream().filter(p -> p .getNombre().contains(nombre)).collect(Collectors.toList());
        model.addAttribute("productos",productos);
        return "usuario/home";
    }
}
