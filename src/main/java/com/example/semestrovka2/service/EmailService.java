package com.example.semestrovka2.service;

import com.example.semestrovka2.EmailConfig;
import com.example.semestrovka2.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


import java.util.UUID;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private IUsuarioService usuarioService;

    public void sendConfirmationEmail(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        usuario.setConfirmationToken(token);
        usuarioService.save(usuario); // Guardar el usuario con el token de confirmación

        String subject = "Confirmación de registro";
        String confirmationUrl = "http://localhost:8080/usuario/confirmarRegistro?token=" + token;
        String text = "Por favor, haz clic en el siguiente enlace para confirmar tu registro: " + confirmationUrl;

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom("tuemail@ejemplo.com");
            helper.setTo(usuario.getEmail());
            helper.setSubject(subject);
            helper.setText(text);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(message);
    }
}

