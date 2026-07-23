package com.controleestoque.controle.controller;

import com.controleestoque.controle.model.Fornecedor;
import com.controleestoque.controle.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @Autowired
    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("fornecedores", fornecedorService.listarTodos());
        return "fornecedores/lista";
    }

    @GetMapping("/novo")
    public String exibirFormularioNovo(Model model) {
        model.addAttribute("fornecedor", new Fornecedor());
        return "fornecedores/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Fornecedor fornecedor, RedirectAttributes redirectAttributes) {
        try {
            fornecedorService.salvar(fornecedor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Fornecedor salvo com sucesso!");
            return "redirect:/fornecedores";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/fornecedores/novo";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("fornecedor", fornecedorService.buscarPorId(id));
            return "fornecedores/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/fornecedores";
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            fornecedorService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Fornecedor excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir fornecedor.");
        }
        return "redirect:/fornecedores";
    }
}
