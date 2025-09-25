package com.example; // Garanta que este pacote é o mesmo das suas outras classes

import com.example.Projeto.StatusProjeto; 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SistemaGestaoApplication {
    public static void main(String[] args) {
        // Esta linha inicia toda a aplicação Spring Boot
        SpringApplication.run(SistemaGestaoApplication.class, args);
    }
}
