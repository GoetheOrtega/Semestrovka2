package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Usuario;
import com.example.semestrovka2.repository.UsuarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final HttpSession session;
    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository, HttpSession session) {
        this.usuarioRepository = usuarioRepository;
        this.session = session;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Cargando usuario por nombre de usuario: {}", username);
        Optional<Usuario> optionalUser = usuarioRepository.findByEmail(username);
        if (optionalUser.isPresent()) {
            Usuario usuario = optionalUser.get();
            session.setAttribute("idusuario", usuario.getId());
            return User.builder()
                    .username(usuario.getNombre())
                    .password(usuario.getPassword())  // No cifrar nuevamente
                    .roles(usuario.getTipo())
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
