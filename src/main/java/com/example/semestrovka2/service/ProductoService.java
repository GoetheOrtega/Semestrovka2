package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    public Producto save(Producto producto);
    public Optional<Producto>get(Integer id);
    public void update(Producto producto);
    public void delete(Integer id);
    public List<Producto> findAll();

    Page<Producto> findPaginated(Pageable pageable);
    }
