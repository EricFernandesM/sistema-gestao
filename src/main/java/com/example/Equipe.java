package com.example;

import java.util.ArrayList; 
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;  

@Entity
@Table(name = "equipes")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da equipe não pode estar em branco") // ADICIONADO
    @Size(min = 3, max = 50, message = "O nome da equipe deve ter entre 3 e 50 caracteres") // ADICIONADO
    private String nome;

    @Size(max = 255, message = "A descrição não pode exceder 255 caracteres") // ADICIONADO (opcional)
    private String descricao;

    @ManyToMany
    @JoinTable(
        name = "equipe_membros",
        joinColumns = @JoinColumn(name = "id_equipe"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<Usuario> membros;

    public Equipe() {
        this.membros = new ArrayList<>();
    }

    public Equipe(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.membros = new ArrayList<>();
    }

    public void adicionarMembro(Usuario membro) {
        this.membros.add(membro);
    }

    // ===== GETTERS E SETTERS =====
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }
}