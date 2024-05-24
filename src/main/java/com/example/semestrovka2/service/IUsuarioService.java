package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Usuario;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;
@Transactional
public interface IUsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByConfirmationToken(String token); // Nuevo método
}
