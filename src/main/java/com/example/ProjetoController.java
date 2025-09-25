package com.example; 

import com.example.PerfilUsuario;
import com.example.Projeto.StatusProjeto; 
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError; 
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    // 1. Exibir todos os projetos
    @GetMapping
    public String listarProjetos(Model model) {
        List<Projeto> projetos = projetoRepository.findAll();
        model.addAttribute("projetos", projetos);
        return "lista-projetos";
    }

    // Método auxiliar para adicionar objetos comuns ao modelo (lista de gerentes, status)
    private void addCommonAttributes(Model model) {

        List<Usuario> gerentes = usuarioRepository.findByPerfil(PerfilUsuario.GERENTE);
        model.addAttribute("gerentes", gerentes); // Renomeei para 'gerentes' para clareza no HTML
        model.addAttribute("statusProjetoEnum", StatusProjeto.values());
        model.addAttribute("equipes", equipeRepository.findAll());
    }

    // 2. Exibir formulário para novo projeto
    @GetMapping("/novo")
    public String exibirFormularioNovoProjeto(Model model) {
        model.addAttribute("projeto", new Projeto());
        addCommonAttributes(model);
        return "form-projeto";
    }

    // 3. Exibir formulário para editar projeto existente
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicaoProjeto(@PathVariable("id") Long id, Model model, RedirectAttributes attributes) {
        Optional<Projeto> projetoOptional = projetoRepository.findById(id);
        if (projetoOptional.isPresent()) {
            model.addAttribute("projeto", projetoOptional.get());
            addCommonAttributes(model);
            return "form-projeto";
        } else {
            attributes.addFlashAttribute("mensagemErro", "Projeto não encontrado para edição.");
            return "redirect:/projetos";
        }
    }

    // 4. Processar o formulário (salvar novo ou atualizar existente)
    @PostMapping
    public String salvarProjeto(@Valid @ModelAttribute("projeto") Projeto projeto, 
                                BindingResult result, @RequestParam(value = "equipeIds", required = false) List<Long> equipeIds, 
                                RedirectAttributes attributes, Model model) {
        // Primeiro, as validações padrão do Bean Validation
        if (result.hasErrors()) {
            addCommonAttributes(model);
            return "form-projeto";
        }
        
        List<Equipe> equipesSelecionadas = null;
        if (equipeIds != null && !equipeIds.isEmpty()) {
            equipesSelecionadas = equipeRepository.findAllById(equipeIds);
        }

        // Limpa as equipes existentes para evitar duplicação ou persistência de equipes removidas
        if (projeto.getId() != null) {
            Optional<Projeto> existingProjetoOpt = projetoRepository.findById(projeto.getId());
            if (existingProjetoOpt.isPresent()) {
                Projeto existingProjeto = existingProjetoOpt.get();
                // Desconecta as equipes antigas do lado da Equipe
                for (Equipe equipe : new ArrayList<>(existingProjeto.getEquipes())) {
                    equipe.getProjetos().remove(existingProjeto);
                }
            }
        }
        projeto.setEquipes(new ArrayList<>()); // Limpa a lista antes de adicionar novas
        if (equipesSelecionadas != null) {
            for (Equipe equipe : equipesSelecionadas) {
                projeto.adicionarEquipe(equipe); // Usa o método utilitário para manter a bidirecionalidade
            }
        }


        // Verificar o perfil do gerente
        if (projeto.getGerenteResponsavel() != null) {
            Optional<Usuario> gerenteOpt = usuarioRepository.findById(projeto.getGerenteResponsavel().getId());
            if (gerenteOpt.isEmpty() || gerenteOpt.get().getPerfil() != PerfilUsuario.GERENTE) {
                // Adiciona um erro específico ao BindingResult
                result.addError(new FieldError("projeto", "gerenteResponsavel", projeto.getGerenteResponsavel().getId(), false, null, null, "O usuário selecionado como gerente não possui o perfil de GERENTE."));
            }
        }

        // Se houver erros APÓS a validação personalizada, retorna
        if (result.hasErrors()) {
            addCommonAttributes(model);
            return "form-projeto";
        }

        projetoRepository.save(projeto);
        attributes.addFlashAttribute("mensagemSucesso", "Projeto salvo com sucesso!");
        return "redirect:/projetos";
    }

    // 5. Método para deletar um projeto
    @GetMapping("/deletar/{id}")
    public String deletarProjeto(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            Optional<Projeto> projetoOptional = projetoRepository.findById(id);
            if (projetoOptional.isPresent()) {
                Projeto projeto = projetoOptional.get();
                for (Equipe equipe : new ArrayList<>(projeto.getEquipes())) {
                    equipe.getProjetos().remove(projeto); // Remove o projeto do lado da equipe
                }
                projeto.getEquipes().clear(); // Limpa a lista de equipes do projeto
            }
            projetoRepository.deleteById(id);
            attributes.addFlashAttribute("mensagemSucesso", "Projeto deletado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("mensagemErro", "Erro ao deletar projeto. Verifique se não há dependências.");
        }
        return "redirect:/projetos";
    }
}