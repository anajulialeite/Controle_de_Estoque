package com.controleestoque.controle.repository;

import com.controleestoque.controle.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.controleestoque.controle.model.TipoMovimentacao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    
    List<MovimentacaoEstoque> findByProdutoId(Long produtoId);

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE 1=1 " +
           "AND (:produtoId IS NULL OR m.produto.id = :produtoId) " +
           "AND (:tipo IS NULL OR m.tipo = :tipo) " +
           "AND (:dataInicio IS NULL OR m.dataHora >= :dataInicio) " +
           "AND (:dataFim IS NULL OR m.dataHora <= :dataFim) " +
           "ORDER BY m.dataHora DESC")
    List<MovimentacaoEstoque> buscarComFiltros(
        @Param("produtoId") Long produtoId, 
        @Param("tipo") TipoMovimentacao tipo, 
        @Param("dataInicio") LocalDateTime dataInicio, 
        @Param("dataFim") LocalDateTime dataFim
    );
}
