package com.controleestoque.controle.service;

import com.controleestoque.controle.model.Produto;
import com.controleestoque.controle.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodosAtivos() {
        return produtoRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Produto> listarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaIdAndAtivoTrue(categoriaId);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));
        if (!produto.getAtivo()) {
            throw new RuntimeException("O produto informado está inativo no sistema.");
        }
        return produto;
    }

    @Transactional
    public Produto salvar(Produto produto) {
        if (produto.getId() == null) {
            // Regras para novo produto
            if (produto.getCodigoBarras() != null && !produto.getCodigoBarras().isEmpty() &&
                produtoRepository.existsByCodigoBarras(produto.getCodigoBarras())) {
                throw new RuntimeException("Já existe um produto cadastrado com o código de barras: " + produto.getCodigoBarras());
            }
            produto.setDataCadastro(LocalDateTime.now());
            produto.setAtivo(true);
            if (produto.getQuantidadeEstoque() == null) {
                produto.setQuantidadeEstoque(0);
            }
            if (produto.getEstoqueMinimo() == null) {
                produto.setEstoqueMinimo(5);
            }
        } else {
            // Regras para atualização de produto existente (impede alteração acidental de estoque ou data de cadastro via update de tela)
            Produto produtoOriginal = produtoRepository.findById(produto.getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + produto.getId()));
            
            // Valida código de barras se tiver sido alterado
            if (produto.getCodigoBarras() != null && !produto.getCodigoBarras().equals(produtoOriginal.getCodigoBarras()) &&
                produtoRepository.existsByCodigoBarras(produto.getCodigoBarras())) {
                throw new RuntimeException("Já existe outro produto cadastrado com o código de barras: " + produto.getCodigoBarras());
            }

            produto.setDataCadastro(produtoOriginal.getDataCadastro());
            produto.setQuantidadeEstoque(produtoOriginal.getQuantidadeEstoque());
            produto.setAtivo(produtoOriginal.getAtivo());
        }

         return produtoRepository.save(produto);
    }

    @Transactional
    public void desativar(Long id) {
        Produto produto = buscarPorId(id);
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarComFiltros(String nome, Long categoriaId, Long fornecedorId) {
        String nomeFiltro = (nome != null && !nome.trim().isEmpty()) ? nome.trim() : null;
        return produtoRepository.buscarComFiltros(nomeFiltro, categoriaId, fornecedorId);
    }

    @Transactional(readOnly = true)
    public List<Produto> listarEstoqueBaixo() {
        return produtoRepository.findByAtivoTrueAndEstoqueBaixo();
    }
}
