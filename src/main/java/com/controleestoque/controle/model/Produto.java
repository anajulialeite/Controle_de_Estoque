package com.controleestoque.controle.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(name = "codigo_barras", unique = true)
    private String codigoBarras;

    @Column(name = "preco_compra", nullable = false)
    private BigDecimal precoCompra;

    @Column(name = "preco_venda", nullable = false)
    private BigDecimal precoVenda;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque = 0;

    @Column(name = "estoque_minimo", nullable = false)
    private Integer estoqueMinimo = 5;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id", nullable = true)
    private Fornecedor fornecedor;

    // Construtor Padrão (JPA)
    public Produto() {
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
        this.quantidadeEstoque = 0;
        this.estoqueMinimo = 5;
    }

    // Construtor sem ID (sem fornecedor - para retrocompatibilidade)
    public Produto(String nome, String descricao, String codigoBarras, BigDecimal precoCompra, 
                   BigDecimal precoVenda, Integer quantidadeEstoque, Integer estoqueMinimo, 
                   Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.codigoBarras = codigoBarras;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque != null ? quantidadeEstoque : 0;
        this.estoqueMinimo = estoqueMinimo != null ? estoqueMinimo : 5;
        this.categoria = categoria;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }

    // Construtor sem ID (com fornecedor)
    public Produto(String nome, String descricao, String codigoBarras, BigDecimal precoCompra, 
                   BigDecimal precoVenda, Integer quantidadeEstoque, Integer estoqueMinimo, 
                   Categoria categoria, Fornecedor fornecedor) {
        this(nome, descricao, codigoBarras, precoCompra, precoVenda, quantidadeEstoque, estoqueMinimo, categoria);
        this.fornecedor = fornecedor;
    }

    // Construtor completo (sem fornecedor - para retrocompatibilidade)
    public Produto(Long id, String nome, String descricao, String codigoBarras, BigDecimal precoCompra, 
                   BigDecimal precoVenda, Integer quantidadeEstoque, Integer estoqueMinimo, 
                   LocalDateTime dataCadastro, Boolean ativo, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.codigoBarras = codigoBarras;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueMinimo = estoqueMinimo;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
        this.categoria = categoria;
    }

    // Construtor completo (com fornecedor)
    public Produto(Long id, String nome, String descricao, String codigoBarras, BigDecimal precoCompra, 
                   BigDecimal precoVenda, Integer quantidadeEstoque, Integer estoqueMinimo, 
                   LocalDateTime dataCadastro, Boolean ativo, Categoria categoria, Fornecedor fornecedor) {
        this(id, nome, descricao, codigoBarras, precoCompra, precoVenda, quantidadeEstoque, estoqueMinimo, dataCadastro, ativo, categoria);
        this.fornecedor = fornecedor;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public BigDecimal getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(BigDecimal precoCompra) {
        this.precoCompra = precoCompra;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Integer getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(Integer estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    // Métodos Auxiliares
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", precoVenda=" + precoVenda +
                ", quantidadeEstoque=" + quantidadeEstoque +
                ", ativo=" + ativo +
                '}';
    }
}
