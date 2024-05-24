package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Producto;
import com.example.semestrovka2.model.Usuario;


import javax.transaction.Transactional;

@Transactional
public interface LikeService {
    void toggleLike(Producto producto, Usuario usuario);
    boolean hasLiked(Producto producto, Usuario usuario);
    void deleteByProductoId(Integer productoId);
}