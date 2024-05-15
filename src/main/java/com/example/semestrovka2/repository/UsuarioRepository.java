package com.example.semestrovka2.repository;

import com.example.semestrovka2.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario,Integer> {
Optional<Usuario> findByEmail(String email);
}
