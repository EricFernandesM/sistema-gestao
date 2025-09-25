package com.example; 

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/equipes")
public class EquipeController {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Injetar o repositório de Usuario

    // 1. Exibir todas as equipes
    @GetMapping
    public String listarEquipes(Model model) {
        List<Equipe> equipes = equipeRepository.findAll();
        model.addAttribute("equipes", equipes);
        return "lista-equipes"; // Seu template HTML de listagem
    }

    // Método auxiliar para adicionar objetos comuns ao modelo (como lista de usuários)
    private void addCommonAttributes(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll(); // Buscar todos os usuários
        model.addAttribute("usuarios", usuarios); // Disponibiliza todos os usuários para seleção
    }

    // 2. Exibir formulário para nova equipe
    @GetMapping("/novo")
    public String exibirFormularioNovaEquipe(Model model) {
        model.addAttribute("equipe", new Equipe());
        addCommonAttributes(model); // Adiciona usuários ao modelo
        return "form-equipe"; // Seu template HTML de formulário
    }

    // 3. Exibir formulário para editar equipe existente
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicaoEquipe(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        Optional<Equipe> equipeOptional = equipeRepository.findById(id);
        if (equipeOptional.isPresent()) {
            model.addAttribute("equipe", equipeOptional.get());
            addCommonAttributes(model); // Adiciona usuários ao modelo
            return "form-equipe"; // Reutiliza o mesmo template de formulário
        } else {
            attributes.addFlashAttribute("mensagemErro", "Equipe não encontrada para edição.");
            return "redirect:/equipes";
        }
    }

    // 4. Processar o formulário (salvar novo ou atualizar existente)
    @PostMapping
    public String salvarEquipe(@Valid @ModelAttribute("equipe") Equipe equipe, BindingResult result,
                               @RequestParam(value = "membroIds", required = false) List<Long> membroIds, // <<<<< NOVO: Captura os IDs dos membros
                               RedirectAttributes attributes, Model model) {
        
        if (result.hasErrors()) {
            addCommonAttributes(model); // Precisa adicionar novamente se retornar para o formulário com erros
            return "form-equipe"; // Retorna ao formulário com os erros
        }

        List<Usuario> membrosSelecionados = null;
        if (membroIds != null && !membroIds.isEmpty()) {
            membrosSelecionados = usuarioRepository.findAllById(membroIds);
        }

        // Limpa os membros existentes para evitar duplicação ou persistência de membros removidos
        // Se for uma equipe existente, desconecte os membros antigos antes de adicionar os novos
        if (equipe.getId() != null) {
            Optional<Equipe> existingEquipeOpt = equipeRepository.findById(equipe.getId());
            if (existingEquipeOpt.isPresent()) {
                Equipe existingEquipe = existingEquipeOpt.get();
                // Desconecta os membros antigos do lado do Usuario
                for (Usuario membro : new ArrayList<>(existingEquipe.getMembros())) {
                    membro.getEquipes().remove(existingEquipe);
                }
            }
        }
        equipe.setMembros(new ArrayList<>()); // Limpa a lista antes de adicionar novos
        if (membrosSelecionados != null) {
            for (Usuario membro : membrosSelecionados) {
                equipe.adicionarMembro(membro); // Usa o método utilitário para manter a bidirecionalidade
            }
        }

        equipeRepository.save(equipe);
        attributes.addFlashAttribute("mensagemSucesso", "Equipe salva com sucesso!");
        return "redirect:/equipes";
    }

    // 5. Método para deletar uma equipe
    @GetMapping("/deletar/{id}")
    public String deletarEquipe(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {

            Optional<Equipe> equipeOptional = equipeRepository.findById(id);
            if (equipeOptional.isPresent()) {
                Equipe equipe = equipeOptional.get();
                for (Usuario membro : new ArrayList<>(equipe.getMembros())) {
                    membro.getEquipes().remove(equipe); // Remove a equipe do lado do usuário
                }
                equipe.getMembros().clear(); // Limpa a lista de membros da equipe
            }

            equipeRepository.deleteById(id);
            attributes.addFlashAttribute("mensagemSucesso", "Equipe deletada com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", "Erro ao deletar equipe. Verifique se não há dependências. " + e.getMessage());
        }
        return "redirect:/equipes";
    }
}