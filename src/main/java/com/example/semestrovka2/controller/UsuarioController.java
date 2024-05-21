package com.example.semestrovka2.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.semestrovka2.model.Orden;
import com.example.semestrovka2.model.Usuario;
import com.example.semestrovka2.service.EmailService;
import com.example.semestrovka2.service.IUsuarioService;
import com.example.semestrovka2.service.OrdenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private EmailService emailService; // Añadir el servicio de correo electrónico

    BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();

    @GetMapping("/registro")
    public String create() {
        return "usuario/registro";
    }

    @PostMapping("/save")
    public String save(Usuario usuario) {
        logger.info("Usuario registro: {}", usuario);
        usuario.setTipo("USER");
        usuario.setPassword(passEncode.encode(usuario.getPassword()));

        // Generar y establecer el token de confirmación
        String token = UUID.randomUUID().toString();
        usuario.setConfirmationToken(token);
        usuario.setRegistrado(false); // Marcar al usuario como no registrado

        usuarioService.save(usuario);

        // Enviar correo electrónico de confirmación
        emailService.sendConfirmationEmail(usuario);

        return "redirect:/";
    }

    @GetMapping("/confirmarRegistro")
    public String confirmRegistration(@RequestParam("token") String token) {
        Optional<Usuario> usuario = usuarioService.findByConfirmationToken(token);
        if (usuario.isPresent()) {
            Usuario user = usuario.get();
            user.setRegistrado(true);
            usuarioService.save(user);
            return "redirect:/usuario/registroConfirmado";
        } else {
            return "redirect:/usuario/registroFallido";
        }
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

    @GetMapping("/registroConfirmado")
    public String registroConfirmado() {
        return "usuario/registroConfirmado";
    }

    @GetMapping("/registroFallido")
    public String registroFallido() {
        return "usuario/registroFallido";
    }
}