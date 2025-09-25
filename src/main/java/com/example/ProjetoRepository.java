package com.example; 

import com.example.Projeto; 
import com.example.Projeto.StatusProjeto; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    // Adicione este método para consultar projetos por status
    List<Projeto> findByStatus(StatusProjeto status);
    long countByStatus(StatusProjeto status); // Método de contagem também usa o enum
}