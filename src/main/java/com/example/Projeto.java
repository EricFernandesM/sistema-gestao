package com.example; 

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList; 
import java.util.List;     

@Entity
@Table(name = "projetos")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY) // Um projeto tem um gerente, um gerente pode ter vários projetos
    @JoinColumn(name = "gerente_id", nullable = false)
    @NotNull(message = "O gerente responsável não pode ser nulo.")
    private Usuario gerenteResponsavel;

    private LocalDate dataInicio;
    private LocalDate dataTerminoPrevista;

    @Enumerated(EnumType.STRING) // Armazena o enum como String no BD
    private StatusProjeto status;

    // Se você adicionou o relacionamento ManyToMany com Equipe
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "projeto_equipes",
        joinColumns = @JoinColumn(name = "projeto_id"),
        inverseJoinColumns = @JoinColumn(name = "equipe_id")
    )
    private List<Equipe> equipes = new ArrayList<>();

    // Construtores (verifique se o construtor que você está usando
    public Projeto() {
    }

    public Projeto(String nome, Usuario gerenteResponsavel, LocalDate dataInicio, LocalDate dataTerminoPrevista, StatusProjeto status) {
        this.nome = nome;
        this.descricao = descricao;
        this.gerenteResponsavel = gerenteResponsavel;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevista = dataTerminoPrevista;
        this.status = status;
    }

    public enum StatusProjeto {
        PLANEJADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO
    }

    // Getters e Setters (Certifique-se de ter para todos os campos)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getGerenteResponsavel() {
        return gerenteResponsavel;
    }

    public void setGerenteResponsavel(Usuario gerenteResponsavel) {
        this.gerenteResponsavel = gerenteResponsavel;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataTerminoPrevista() {
        return dataTerminoPrevista;
    }

    public void setDataTerminoPrevista(LocalDate dataTerminoPrevista) {
        this.dataTerminoPrevista = dataTerminoPrevista;
    }

    public StatusProjeto getStatus() {
        return status;
    }

    public void setStatus(StatusProjeto status) {
        this.status = status;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }

    public void adicionarEquipe(Equipe equipe) {
        if (this.equipes == null) {
            this.equipes = new ArrayList<>();
        }
        this.equipes.add(equipe);
    }

    public String getDescricao() {
    return descricao;
}

public void setDescricao(String descricao) {
    this.descricao = descricao;
}
}