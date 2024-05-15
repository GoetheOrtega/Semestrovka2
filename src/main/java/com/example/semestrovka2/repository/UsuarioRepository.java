package com.example.semestrovka2.repository;

import com.example.semestrovka2.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario,Integer> {

}
