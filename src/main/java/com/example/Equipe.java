package com.example;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; // Para validação de campos de texto
import jakarta.validation.constraints.Size;    // Para validação de tamanho

import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // Para equals/hashCode

@Entity
@Table(name = "equipes")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da equipe é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome da equipe deve ter entre 2 e 100 caracteres.")
    private String nome;

    @Size(max = 500, message = "A descrição pode ter no máximo 500 caracteres.")
    private String descricao;

    // <<<<<<<<<<< NOVO: Relacionamento ManyToMany com Usuário >>>>>>>>>>>>>
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "equipe_membros", // Nome da tabela intermediária
        joinColumns = @JoinColumn(name = "equipe_id"), // Coluna que referencia esta entidade (Equipe)
        inverseJoinColumns = @JoinColumn(name = "usuario_id") // Coluna que referencia a outra entidade (Usuario)
    )
    private List<Usuario> membros = new ArrayList<>(); // Lista de usuários nesta equipe

    // Uma equipe pode atuar em vários projetos
    // mappedBy indica que a propriedade 'equipes' na entidade Projeto é o lado "proprietário"
    // e esta é a parte "inversa".
    @ManyToMany(mappedBy = "equipes")
    private List<Projeto> projetos = new ArrayList<>();


    // Construtor padrão (obrigatório para JPA)
    public Equipe() {
    }

    // Construtor com campos básicos (opcional)
    public Equipe(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters e Setters
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

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    // Métodos utilitários para adicionar/remover membros
    public void adicionarMembro(Usuario usuario) {
        if (this.membros == null) {
            this.membros = new ArrayList<>();
        }
        this.membros.add(usuario);
        usuario.getEquipes().add(this); // Mantém o lado do Usuario sincronizado
    }

    public void removerMembro(Usuario usuario) {
        if (this.membros != null) {
            this.membros.remove(usuario);
            usuario.getEquipes().remove(this); // Mantém o lado do Usuario sincronizado
        }
    }


    // Sobrescrever equals e hashCode para entidades com relacionamento Many-to-Many
    // para garantir que Collections funcionem corretamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipe equipe = (Equipe) o;
        return Objects.equals(id, equipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}