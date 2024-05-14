package com.example.semestrovka2.controller;

import com.example.semestrovka2.model.Producto;
import com.example.semestrovka2.model.Usuario;
import com.example.semestrovka2.service.ProductoService;
import com.example.semestrovka2.service.UploadFileService;
import org.slf4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService uploadFileService;
    @GetMapping(" ")
    public  String show(Model model){

    model.addAttribute("productos",productoService.findAll());
        return "/show";
    }
    @GetMapping("/create")

    public String create(){
        return "create";
    }
    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        LOGGER.info("objeto producto{}", producto);
        Usuario u = new Usuario(1, "", "", "", "", "", "", "");
        producto.setUsuario(u);
        //imagen
        if (producto.getId() == null) {// cuando se crea un producto
            String nombreImagen = uploadFileService.saveImage(file);
            producto.setImagen(nombreImagen);
        } else {

        }
            productoService.save(producto);
            return "redirect:/productos";
        }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto= new Producto();
        Optional<Producto> optionalProducto=productoService.get(id);
        producto= optionalProducto.get();

        LOGGER.info("Producto buscado: {}",producto);
        model.addAttribute("producto", producto);

        return "/edit";
    }
    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file ) throws IOException {
        Producto p= new Producto();
        p=productoService.get(producto.getId()).get();

        if (file.isEmpty()) { // editamos el producto pero no cambiamos la imagem

            producto.setImagen(p.getImagen());
            //editar tambien la imagen
        }else {
           // si la imagen no es por defecto se elimina
            if (!p.getImagen().equals("proyecto imagen sin cargar.png")) {
                uploadFileService.deleteImage(p.getImagen());
            }
            String nombreImagen= uploadFileService.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());
        productoService.update(producto);
        return "redirect:/productos";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        Producto p = new Producto();
        p = productoService.get(id).get();
        if (!p.getImagen().equals("proyecto imagen sin cargar.png")){
            uploadFileService.deleteImage(p.getImagen());
        }

        productoService.delete(id);
        return "redirect:/productos";
    }
}
