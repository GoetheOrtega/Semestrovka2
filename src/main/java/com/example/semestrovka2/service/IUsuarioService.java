package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Usuario;

import java.util.Optional;

public interface IUsuarioService {
   Optional<Usuario> findById(Integer id);

}
