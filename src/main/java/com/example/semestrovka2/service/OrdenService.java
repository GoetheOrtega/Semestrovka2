package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Orden;

import java.util.List;

public interface OrdenService {
    List<Orden> findAll();
    Orden save(Orden Orden);
    String generarNumeroOrden();
}
