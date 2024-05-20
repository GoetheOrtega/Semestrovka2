package com.example.semestrovka2.controller;

import java.util.List;
import java.util.Optional;

import com.example.semestrovka2.model.Orden;
import com.example.semestrovka2.model.Usuario;
import com.example.semestrovka2.service.IUsuarioService;
import com.example.semestrovka2.service.OrdenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final Logger logger= LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    BCryptPasswordEncoder passEncode= new BCryptPasswordEncoder();

    @GetMapping("/registro")
    public String create() {
        return "usuario/registro";
    }

    @PostMapping("/save")
    public String save(Usuario usuario) {
        logger.info("Usuario registro: {}", usuario);
        usuario.setTipo("USER");
        usuario.setPassword(passEncode.encode(usuario.getPassword()));
        usuarioService.save(usuario);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "usuario/login";
    }

    @GetMapping("/acceder")
    public String acceder(HttpSession session) {
        Integer idUsuario = (Integer) session.getAttribute("idusuario");
        if (idUsuario != null) {
            Optional<Usuario> user = usuarioService.findById(idUsuario);
            if (user.isPresent()) {
                if ("ADMIN".equals(user.get().getTipo())) {
                    return "redirect:/administrador";
                } else {
                    return "redirect:/";
                }
            } else {
                logger.info("Usuario no existe");
            }
        }
        return "redirect:/usuario/login?error";
    }

    @GetMapping("/compras")
    public String obtenerCompras(Model model, HttpSession session) {
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).orElse(null);
        if (usuario != null) {
            List<Orden> ordenes = ordenService.findByUsuario(usuario);
            logger.info("ordenes {}", ordenes);
            model.addAttribute("ordenes", ordenes);
        }
        return "usuario/compras";
    }

    @GetMapping("/detalle/{id}")
    public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
        logger.info("Id de la orden: {}", id);
        Optional<Orden> orden = ordenService.findById(id);

        model.addAttribute("detalles", orden.orElse(new Orden()).getDetalle());
        model.addAttribute("sesion", session.getAttribute("idusuario"));
        return "usuario/detallecompra";
    }

    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session) {
        session.removeAttribute("idusuario");
        return "redirect:/";
    }
}
