package com.example; // Mantenha seu pacote original

import com.example.Equipe;
import com.example.EquipeRepository;

import jakarta.validation.Valid; // Importe esta anotação para validação
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Importe para lidar com resultados de validação
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable; // Importe para capturar IDs da URL
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping; // Importe para agrupar as rotas
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Importe para mensagens flash

import java.util.List; // Importe para listas
import java.util.Optional; // Importe para lidar com a ausência de um ID no banco

@Controller
@RequestMapping("/equipes") // Agrupa todas as rotas de equipes sob /equipes
public class EquipeController {

    @Autowired
    private EquipeRepository equipeRepository;

    // 1. Método para MOSTRAR a lista de todas as equipes
    @GetMapping // Acessível via /equipes
    public String listarEquipes(Model model) {
        List<Equipe> equipes = equipeRepository.findAll();
        model.addAttribute("equipes", equipes);
        return "lista-equipes"; // Aponta para o arquivo lista-equipes.html
    }

    // 2. Método para MOSTRAR o formulário de CRIAÇÃO de uma nova equipe
    @GetMapping("/nova") // Acessível via /equipes/nova
    public String mostrarFormularioDeEquipe(Model model) {
        model.addAttribute("equipe", new Equipe()); // Cria uma nova equipe vazia
        return "form-equipe"; // Aponta para o arquivo form-equipe.html
    }

    // 3. Método para MOSTRAR o formulário de EDIÇÃO de uma equipe existente
    // Acessível via /equipes/editar/1, /equipes/editar/2, etc.
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicaoEquipe(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        Optional<Equipe> equipeOptional = equipeRepository.findById(id); // Busca a equipe pelo ID

        if (equipeOptional.isPresent()) {
            model.addAttribute("equipe", equipeOptional.get()); // Adiciona a equipe encontrada ao modelo
            return "form-equipe"; // Reutiliza o mesmo template de formulário para edição
        } else {
            // Se a equipe não for encontrada, redireciona com uma mensagem de erro
            attributes.addFlashAttribute("mensagemErro", "Equipe não encontrada para edição.");
            return "redirect:/equipes";
        }
    }

    // 4. Método para SALVAR/ATUALIZAR a equipe (recebe dados do formulário)
    // Este método lida tanto com a criação quanto com a edição
    @PostMapping // Acessível via POST para /equipes
    public String salvarEquipe(@Valid @ModelAttribute("equipe") Equipe equipe, BindingResult result, RedirectAttributes attributes) {
        // Verifica se há erros de validação (ex: @NotBlank, @Size na classe Equipe)
        if (result.hasErrors()) {
            // Se houver erros, retorna ao formulário para que o usuário os corrija
            return "form-equipe";
        }

        equipeRepository.save(equipe); // O JPA se encarrega de salvar (se id nulo) ou atualizar (se id presente)
        attributes.addFlashAttribute("mensagemSucesso", "Equipe salva com sucesso!"); // Mensagem de sucesso
        return "redirect:/equipes"; // Redireciona para a página de listagem
    }

    // 5. Método para DELETAR uma equipe
    // Acessível via /equipes/deletar/1, /equipes/deletar/2, etc. (normalmente chamado por um link)
    @GetMapping("/deletar/{id}")
    public String deletarEquipe(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            equipeRepository.deleteById(id);
            attributes.addFlashAttribute("mensagemSucesso", "Equipe deletada com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", "Erro ao deletar equipe. Verifique se não há dependências.");
        }
        return "redirect:/equipes"; // Redireciona para a página de listagem
    }
}