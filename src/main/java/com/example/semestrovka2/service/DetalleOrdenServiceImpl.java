package com.example.semestrovka2.service;

import com.example.semestrovka2.model.DetalleOrden;
import com.example.semestrovka2.repository.DetalleOrdenRepository;
import com.example.semestrovka2.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleOrdenServiceImpl implements DetalleOrdenService {
    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;
    @Override
    public DetalleOrden save(DetalleOrden detalleOrden) {
        return detalleOrdenRepository.save(detalleOrden);
    }
}
