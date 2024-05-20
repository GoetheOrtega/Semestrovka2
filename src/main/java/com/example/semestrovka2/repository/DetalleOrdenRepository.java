package com.example.semestrovka2.repository;

import com.example.semestrovka2.model.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM DetalleOrden d WHERE d.producto.id = :productoId")
    void deleteByProductoId(@Param("productoId") Integer productoId);
}
