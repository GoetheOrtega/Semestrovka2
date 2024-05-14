package com.example.semestrovka2.controller;

import com.example.semestrovka2.model.Producto;
import com.example.semestrovka2.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public String home(Model model) {
        //cuando haga la peticion al metodo de arriba nos traera los productos de vuelta

        List<Producto>  productos= productoService.findAll();
        //nos lo devuelve a la lista home
        model.addAttribute("productos",productos);
        return "home_page";
    }

}