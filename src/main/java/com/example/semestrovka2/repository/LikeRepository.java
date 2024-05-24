package com.example.semestrovka2.repository;

import com.example.semestrovka2.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<Like, Integer> {
    boolean existsByUsuarioIdAndProductoId(Integer usuarioId, Integer productoId);
    void deleteByUsuarioIdAndProductoId(Integer usuarioId, Integer productoId);

    void deleteByProductoId(Integer productoId);
}