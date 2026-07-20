package com.controleestoque.controle.service;

import com.controleestoque.controle.model.MovimentacaoEstoque;
import com.controleestoque.controle.model.Produto;
import com.controleestoque.controle.model.TipoMovimentacao;
import com.controleestoque.controle.repository.MovimentacaoRepository;
import com.controleestoque.controle.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    @Autowired
    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, ProdutoRepository produtoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoque> listarTodas() {
        return movimentacaoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoque> buscarPorProduto(Long produtoId) {
        return movimentacaoRepository.findByProdutoId(produtoId);
    }

    @Transactional
    public MovimentacaoEstoque registrarMovimentacao(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getProduto() == null || movimentacao.getProduto().getId() == null) {
            throw new RuntimeException("É necessário informar um produto válido para a movimentação.");
        }

        Produto produto = produtoRepository.findById(movimentacao.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + movimentacao.getProduto().getId()));

        if (!produto.getAtivo()) {
            throw new RuntimeException("Não é possível realizar movimentações em um produto inativo.");
        }

        if (movimentacao.getQuantidade() == null || movimentacao.getQuantidade() <= 0) {
            throw new RuntimeException("A quantidade da movimentação deve ser maior que zero.");
        }

        if (movimentacao.getTipo() == null) {
            throw new RuntimeException("O tipo da movimentação (ENTRADA ou SAIDA) deve ser informado.");
        }

        // Lógica de estoque
        if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + movimentacao.getQuantidade());
        } else if (movimentacao.getTipo() == TipoMovimentacao.SAIDA) {
            int novoSaldo = produto.getQuantidadeEstoque() - movimentacao.getQuantidade();
            if (novoSaldo < 0) {
                throw new RuntimeException("Saldo insuficiente para realizar a saída.");
            }
            produto.setQuantidadeEstoque(novoSaldo);
        }

        // Atualiza a data/hora e salva o produto com a nova quantidade
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setProduto(produto);
        
        produtoRepository.save(produto);
        return movimentacaoRepository.save(movimentacao);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoque> buscarComFiltros(Long produtoId, TipoMovimentacao tipo, java.time.LocalDate dataInicio, java.time.LocalDate dataFim) {
        LocalDateTime inicio = (dataInicio != null) ? dataInicio.atStartOfDay() : null;
        LocalDateTime fim = (dataFim != null) ? dataFim.atTime(java.time.LocalTime.MAX) : null;
        return movimentacaoRepository.buscarComFiltros(produtoId, tipo, inicio, fim);
    }
}
