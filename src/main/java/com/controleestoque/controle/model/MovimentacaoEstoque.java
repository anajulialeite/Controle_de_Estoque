package com.controleestoque.controle.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    private String observacao;

    // Construtor Padrão (JPA)
    public MovimentacaoEstoque() {
        this.dataHora = LocalDateTime.now();
    }

    // Construtor sem ID
    public MovimentacaoEstoque(Produto produto, TipoMovimentacao tipo, Integer quantidade, String observacao) {
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.observacao = observacao;
        this.dataHora = LocalDateTime.now();
    }

    // Construtor completo
    public MovimentacaoEstoque(Long id, Produto produto, TipoMovimentacao tipo, Integer quantidade, 
                               LocalDateTime dataHora, String observacao) {
        this.id = id;
        this.produto = produto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
        this.observacao = observacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    // Métodos Auxiliares
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovimentacaoEstoque that = (MovimentacaoEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MovimentacaoEstoque{" +
                "id=" + id +
                ", produto=" + (produto != null ? produto.getNome() : null) +
                ", tipo=" + tipo +
                ", quantidade=" + quantidade +
                ", dataHora=" + dataHora +
                '}';
    }
}
