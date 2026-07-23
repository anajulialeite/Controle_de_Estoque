package com.controleestoque.controle.controller;

import com.controleestoque.controle.model.Categoria;
import com.controleestoque.controle.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "categorias/lista";
    }

    @GetMapping("/nova")
    public String exibirFormularioNova(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.salvar(categoria);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Categoria salva com sucesso!");
            return "redirect:/categorias";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/categorias/nova";
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("categoria", categoriaService.buscarPorId(id));
            return "categorias/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/categorias";
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Categoria excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir categoria. Certifique-se de que não existem produtos vinculados a ela.");
        }
        return "redirect:/categorias";
    }
}
