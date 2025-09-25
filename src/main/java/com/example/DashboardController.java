package com.example;

import com.example.Projeto;
import com.example.Projeto.StatusProjeto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    // Mapeia a URL raiz do site (ex: http://localhost:8080/) para este método
    @GetMapping("/")
    public String mostrarDashboard(Model model) {
        // Busca os totais de cada entidade
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalProjetos", projetoRepository.count());
        model.addAttribute("totalEquipes", equipeRepository.count());

        // Busca uma lista de projetos que estão "EM_ANDAMENTO" para exibir no dashboard
        model.addAttribute("projetosEmAndamento", projetoRepository.findByStatus(StatusProjeto.EM_ANDAMENTO));
        model.addAttribute("projetosPlanejados", projetoRepository.findByStatus(StatusProjeto.PLANEJADO));
        model.addAttribute("projetosConcluidos", projetoRepository.findByStatus(StatusProjeto.CONCLUIDO));

        // Retorna o nome do arquivo HTML que deve ser renderizado
        return "dashboard";
    }
}