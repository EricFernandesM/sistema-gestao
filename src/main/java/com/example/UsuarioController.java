package com.example;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
public String salvarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, 
                           BindingResult result, 
                           RedirectAttributes attributes) {
    
    // 1. Verifica se o BindingResult capturou algum erro de validação
    if (result.hasErrors()) {
        // Se houver erros, retorna para a mesma página do formulário.
        // O Thymeleaf usará o 'result' para exibir as mensagens de erro nos campos corretos.
        return "form-usuario"; 
    }

    // 2. Se não houver erros, salva o usuário no banco de dados
    usuarioRepository.save(usuario);

    // 3. Adiciona uma mensagem de sucesso que será exibida na página de listagem
    attributes.addFlashAttribute("mensagemSucesso", "Usuário salvo com sucesso!");

    // 4. Redireciona para a lista de usuários
    return "redirect:/usuarios";
}
}