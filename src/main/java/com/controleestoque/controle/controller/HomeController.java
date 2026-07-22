package com.controleestoque.controle.controller;

import com.controleestoque.controle.model.MovimentacaoEstoque;
import com.controleestoque.controle.model.Produto;
import com.controleestoque.controle.service.CategoriaService;
import com.controleestoque.controle.service.FornecedorService;
import com.controleestoque.controle.service.MovimentacaoService;
import com.controleestoque.controle.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;
    private final MovimentacaoService movimentacaoService;

    @Autowired
    public HomeController(ProdutoService produtoService, CategoriaService categoriaService,
                          FornecedorService fornecedorService, MovimentacaoService movimentacaoService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Produto> produtosAtivos = produtoService.listarTodosAtivos();
        List<MovimentacaoEstoque> todasMovimentacoes = movimentacaoService.listarTodas();
        
        long totalProdutos = produtosAtivos.size();
        long totalCategorias = categoriaService.listarTodas().size();
        long totalFornecedores = fornecedorService.listarTodos().size();
        
        // Produtos com estoque menor ou igual ao estoque mínimo
        long estoqueBaixo = produtosAtivos.stream()
                .filter(p -> p.getQuantidadeEstoque() <= p.getEstoqueMinimo())
                .count();

        // Calcular custo total, valor total de venda e lucro potencial
        BigDecimal custoTotalEstoque = produtosAtivos.stream()
                .map(p -> p.getPrecoCompra().multiply(BigDecimal.valueOf(p.getQuantidadeEstoque())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorVendaEstoque = produtosAtivos.stream()
                .map(p -> p.getPrecoVenda().multiply(BigDecimal.valueOf(p.getQuantidadeEstoque())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lucroPotencial = valorVendaEstoque.subtract(custoTotalEstoque);

        // Últimas 5 movimentações de estoque
        List<MovimentacaoEstoque> ultimasMovimentacoes = todasMovimentacoes.stream()
                .sorted((m1, m2) -> m2.getDataHora().compareTo(m1.getDataHora()))
                .limit(5)
                .collect(Collectors.toList());

        // Dados para o Gráfico 1: Top 5 produtos com maior estoque
        List<Produto> top5Produtos = produtosAtivos.stream()
                .sorted((p1, p2) -> p2.getQuantidadeEstoque().compareTo(p1.getQuantidadeEstoque()))
                .limit(5)
                .collect(Collectors.toList());

        // Dados para o Gráfico 2: Total de Entradas vs Saídas
        long totalEntradas = todasMovimentacoes.stream()
                .filter(m -> m.getTipo() == com.controleestoque.controle.model.TipoMovimentacao.ENTRADA)
                .mapToLong(MovimentacaoEstoque::getQuantidade)
                .sum();
        long totalSaidas = todasMovimentacoes.stream()
                .filter(m -> m.getTipo() == com.controleestoque.controle.model.TipoMovimentacao.SAIDA)
                .mapToLong(MovimentacaoEstoque::getQuantidade)
                .sum();

        model.addAttribute("totalProdutos", totalProdutos);
        model.addAttribute("totalCategorias", totalCategorias);
        model.addAttribute("totalFornecedores", totalFornecedores);
        model.addAttribute("estoqueBaixo", estoqueBaixo);
        model.addAttribute("custoTotalEstoque", custoTotalEstoque);
        model.addAttribute("valorVendaEstoque", valorVendaEstoque);
        model.addAttribute("lucroPotencial", lucroPotencial);
        model.addAttribute("ultimasMovimentacoes", ultimasMovimentacoes);
        model.addAttribute("top5Produtos", top5Produtos);
        model.addAttribute("totalEntradas", totalEntradas);
        model.addAttribute("totalSaidas", totalSaidas);

        return "home";
    }
}
