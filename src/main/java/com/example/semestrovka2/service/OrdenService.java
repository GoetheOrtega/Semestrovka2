package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Orden;
import com.example.semestrovka2.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface OrdenService {
    List<Orden> findAll();
    Orden save(Orden Orden);
    String generarNumeroOrden();
    List<Orden> findByUsuario(Usuario usuario);

    Optional<Orden> findById(Integer id);
}
