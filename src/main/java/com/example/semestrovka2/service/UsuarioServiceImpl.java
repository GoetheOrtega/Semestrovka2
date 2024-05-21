package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Orden;
import com.example.semestrovka2.model.Usuario;
import com.example.semestrovka2.repository.OrdenRepository;
import com.example.semestrovka2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Optional<Usuario> findByConfirmationToken(String token) {
        return usuarioRepository.findByConfirmationToken(token);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

}

