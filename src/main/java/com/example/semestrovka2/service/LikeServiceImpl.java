package com.example.semestrovka2.service;

import com.example.semestrovka2.model.Like;
import com.example.semestrovka2.model.Producto;
import com.example.semestrovka2.model.Usuario;
import com.example.semestrovka2.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public void toggleLike(Producto producto, Usuario usuario) {
        Like like = new Like();
        like.setProducto(producto);
        like.setUsuario(usuario);
        likeRepository.save(like);
    }

    @Override
    public boolean hasLiked(Producto producto, Usuario usuario) {
        Integer productoId = producto.getId(); // Assumed to be Integer
        Integer usuarioId = usuario.getId();   // Assumed to be Integer

        return likeRepository.existsByUsuarioIdAndProductoId(usuarioId, productoId);
    }
    @Override
    public void deleteByProductoId(Integer productoId) {
        likeRepository.deleteByProductoId(productoId);
    }
}
