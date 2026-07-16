package com.controleestoque.controle.repository;

import com.controleestoque.controle.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    List<Produto> findByAtivoTrue();
    
    List<Produto> findByCategoriaIdAndAtivoTrue(Long categoriaId);
    
    boolean existsByCodigoBarras(String codigoBarras);

    @Query("SELECT p FROM Produto p WHERE p.ativo = true " +
           "AND (:nome IS NULL OR :nome = '' OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
           "AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId) " +
           "AND (:fornecedorId IS NULL OR p.fornecedor.id = :fornecedorId)")
    List<Produto> buscarComFiltros(
        @Param("nome") String nome, 
        @Param("categoriaId") Long categoriaId, 
        @Param("fornecedorId") Long fornecedorId
    );

    @Query("SELECT p FROM Produto p WHERE p.ativo = true AND p.quantidadeEstoque <= p.estoqueMinimo")
    List<Produto> findByAtivoTrueAndEstoqueBaixo();
}
