package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Producto;
import com.example.semestrovka2.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Override
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> get(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public void update(Producto producto) {
        productoRepository.save(producto);

    }
    @Override
    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }
    @Override
    public Page<Producto> findPaginated(Pageable pageable) {
        return productoRepository.findAll(pageable);


    }
    @Override
    public Optional<Producto> findById(Integer id) {
        return productoRepository.findById(id);
    }

}
