package com.example.semestrovka2.controller;

import com.example.semestrovka2.model.Producto;
import com.example.semestrovka2.model.Usuario;
import com.example.semestrovka2.service.IUsuarioService;
import com.example.semestrovka2.service.ProductoService;
import com.example.semestrovka2.service.UploadFileService;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private UploadFileService upload;


    @GetMapping("")
    public String show(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 1); // Mostrar solo un producto por página
        Page<Producto> productoPage = productoService.findPaginated(pageable);
        model.addAttribute("productoPage", productoPage);
        model.addAttribute("currentPage", page);
        return "productos/show";
    }

    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
        LOGGER.info("Este es el objeto producto {}", producto);

        Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
        producto.setUsuario(u);

        // imagen
        if (producto.getId() == null) { // cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }

        productoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto = productoService.get(id).orElse(new Producto());
        LOGGER.info("Producto buscado: {}", producto);
        model.addAttribute("producto", producto);
        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        Producto p = productoService.get(producto.getId()).orElse(new Producto());

        if (file.isEmpty()) { // editamos el producto pero no cambiamos la imagen
            producto.setImagen(p.getImagen());
        } else { // cuando se edita también la imagen
            // eliminar cuando no sea la imagen por defecto
            if (!p.getImagen().equals("proyecto imagen sin cargar.png")) {
                upload.deleteImage(p.getImagen());
            }
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Producto p = productoService.get(id).orElse(new Producto());

        // eliminar cuando no sea la imagen por defecto
        if (!p.getImagen().equals("proyecto imagen sin cargar.png")) {
            upload.deleteImage(p.getImagen());
        }

        productoService.delete(id);
        return "redirect:/productos";
    }
    @JsonBackReference
    @GetMapping("/images")
    @ResponseBody
    public List<Producto> getProductImages() {
        return productoService.findAll();
    }
}