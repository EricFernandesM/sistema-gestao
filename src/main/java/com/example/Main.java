/*package com.example;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("### Iniciando Sistema de Gestão de Projetos ###");

        // 1. CADASTRO DE USUÁRIOS
        System.out.println("\n--- Cadastrando Usuários ---");
        Usuario gerenteAna = new Usuario("Ana Silva", "111.111.111-11", "ana.silva@email.com", "Gerente de Projetos", "ana.silva", "senha123", PerfilUsuario.GERENTE);
        Usuario colabCarla = new Usuario("Carla Dias", "222.222.222-22", "carla.dias@email.com", "Desenvolvedora Backend", "carla.dias", "senha456", PerfilUsuario.COLABORADOR);
        Usuario colabDaniel = new Usuario("Daniel Souza", "333.333.333-33", "daniel.souza@email.com", "Designer de Interface", "daniel.souza", "senha789", PerfilUsuario.COLABORADOR);
        
        System.out.println("Usuários cadastrados com sucesso!");
        System.out.println(gerenteAna);

        System.out.println(colabCarla);
        System.out.println(colabDaniel);

        // 2. CADASTRO DE EQUIPES
        System.out.println("\n--- Cadastrando Equipes e Alocando Membros ---");
        Equipe equipeAlfa = new Equipe("Equipe Alfa", "Focada no desenvolvimento do backend");
        equipeAlfa.adicionarMembro(colabCarla); // Adicionando Carla à equipe Alfa

        Equipe equipeBeta = new Equipe("Equipe Beta", "Focada na experiência do usuário e interface");
        equipeBeta.adicionarMembro(colabDaniel); // Adicionando Daniel à equipe Beta

        System.out.println(equipeAlfa);
        System.out.println(equipeBeta);

        // 3. CADASTRO DE PROJETOS
        System.out.println("\n--- Cadastrando Projetos e Atribuindo Responsáveis e Equipes ---");
        
        // Criando Projeto 1, gerenciado por Ana
        Projeto projetoPhoenix = new Projeto(
            "Projeto Phoenix - Novo Sistema de Vendas", 
            "Desenvolvimento do novo e-commerce da empresa.", 
            LocalDate.of(2025, 10, 1), 
            LocalDate.of(2026, 3, 31), 
            gerenteAna
        );

        // Alocando as duas equipes ao Projeto Phoenix
        projetoPhoenix.adicionarEquipe(equipeAlfa);
        projetoPhoenix.adicionarEquipe(equipeBeta);

        // Criando Projeto 2, também gerenciado por Ana
        Projeto projetoNetuno = new Projeto(
            "Projeto Netuno - App Mobile", 
            "Criação do aplicativo móvel para clientes.", 
            LocalDate.of(2026, 4, 15), 
            LocalDate.of(2026, 9, 30), 
            gerenteAna
        );

        // Alocando apenas a Equipe Beta ao Projeto Netuno
        projetoNetuno.adicionarEquipe(equipeBeta);

        // Exibindo o resultado final
        System.out.println(projetoPhoenix);
        System.out.println(projetoNetuno);
        
        System.out.println("\n### Simulação Concluída ###");
    }
}*/