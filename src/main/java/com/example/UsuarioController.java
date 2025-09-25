package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // Anotação para classes que controlam a interface web
public class UsuarioController {

    @Autowired // O Spring injeta automaticamente nosso repository aqui
    private UsuarioRepository usuarioRepository;

    // Método para MOSTRAR a lista de todos os usuários
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "lista-usuarios"; // Retorna o nome do arquivo HTML
    }

    // Método para MOSTRAR o formulário de criação de um novo usuário
    @GetMapping("/usuarios/novo")
    public String mostrarFormularioDeCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "form-usuario"; // Retorna o nome do arquivo HTML
    }

    // Método para SALVAR o novo usuário que veio do formulário
    @PostMapping("/usuarios")
    public String salvarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        usuarioRepository.save(usuario);
        return "redirect:/usuarios"; // Redireciona para a página de listagem
    }
}