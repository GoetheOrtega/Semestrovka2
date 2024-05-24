package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Transactional
public interface ProductoService {
    Producto save(Producto producto);
    Optional<Producto> get(Integer id); // Cambiar de Long a Integer
    void update(Producto producto);
    void delete(Integer id);
    List<Producto> findAll();

    Page<Producto> findPaginated(Pageable pageable);
    Optional<Producto> findById(Integer id); // Mantener para otros usos, como paginación
}