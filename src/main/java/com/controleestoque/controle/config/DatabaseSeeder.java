package com.controleestoque.controle.config;

import com.controleestoque.controle.model.Categoria;
import com.controleestoque.controle.model.Fornecedor;
import com.controleestoque.controle.model.Produto;
import com.controleestoque.controle.model.MovimentacaoEstoque;
import com.controleestoque.controle.model.TipoMovimentacao;
import com.controleestoque.controle.service.CategoriaService;
import com.controleestoque.controle.service.FornecedorService;
import com.controleestoque.controle.service.ProdutoService;
import com.controleestoque.controle.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;
    private final MovimentacaoService movimentacaoService;

    @Autowired
    public DatabaseSeeder(CategoriaService categoriaService, FornecedorService fornecedorService, 
                          ProdutoService produtoService, MovimentacaoService movimentacaoService) {
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
        this.movimentacaoService = movimentacaoService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Se a base de dados estiver vazia (primeira execução), semeia dados padrão
        if (categoriaService.listarTodas().isEmpty()) {
            System.out.println("Semeando dados iniciais no banco de dados...");

            // 1. Criar Categorias
            Categoria bebidas = new Categoria("Bebidas", "Sucos, refrigerantes, água e energéticos");
            bebidas = categoriaService.salvar(bebidas);

            Categoria alimentos = new Categoria("Alimentos", "Produtos alimentícios em geral");
            alimentos = categoriaService.salvar(alimentos);

            // 2. Criar Fornecedores
            Fornecedor distribuidora = new Fornecedor("Distribuidora Coca-Cola S/A", "00.123.456/0001-99", "(11) 5555-1234", "pedidos@cocacola.com", "Rodovia das Bebidas, Km 12");
            distribuidora = fornecedorService.salvar(distribuidora);

            Fornecedor atacado = new Fornecedor("Alimentos do Brasil S/A", "99.876.543/0001-00", "(21) 4444-5678", "contato@alimentosbrasil.com", "Av. Central de Abastecimento, 1000");
            atacado = fornecedorService.salvar(atacado);

            // 3. Criar Produtos padrão com estoque zerado
            Produto cocacola = new Produto(
                    "Coca-Cola 2L",
                    "Refrigerante de cola garrafa de 2 litros",
                    "7891234560001",
                    new BigDecimal("5.50"),
                    new BigDecimal("8.99"),
                    0, // inicial 0, atualizado por movimentações
                    5,  // estoque mínimo
                    bebidas,
                    distribuidora
            );
            cocacola = produtoService.salvar(cocacola);

            Produto arroz = new Produto(
                    "Arroz Branco 5kg",
                    "Arroz agulhinha tipo 1 pacote de 5kg",
                    "7891234560002",
                    new BigDecimal("18.00"),
                    new BigDecimal("24.90"),
                    0, // inicial 0, atualizado por movimentações
                    3, // estoque mínimo
                    alimentos,
                    atacado
            );
            arroz = produtoService.salvar(arroz);

            // 4. Registrar Movimentações iniciais
            // Coca-Cola: Entrada de 15, Saída de 12 (Saldo final: 3 - Alerta de estoque baixo ativo)
            MovimentacaoEstoque m1 = new MovimentacaoEstoque(cocacola, TipoMovimentacao.ENTRADA, 15, "Recebimento de lote do fornecedor");
            movimentacaoService.registrarMovimentacao(m1);

            MovimentacaoEstoque m2 = new MovimentacaoEstoque(cocacola, TipoMovimentacao.SAIDA, 12, "Venda cliente balcão - NF-e 4501");
            movimentacaoService.registrarMovimentacao(m2);

            // Arroz: Entrada de 8 (Saldo final: 8 - Sem alertas)
            MovimentacaoEstoque m3 = new MovimentacaoEstoque(arroz, TipoMovimentacao.ENTRADA, 8, "Abastecimento estoque quinzenal");
            movimentacaoService.registrarMovimentacao(m3);

            System.out.println("Dados iniciais e movimentações semeados com sucesso!");
        }
    }
}
