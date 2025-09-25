package com.example; // Seu pacote

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Adicionar este método
    List<Usuario> findByPerfil(PerfilUsuario perfil);
}