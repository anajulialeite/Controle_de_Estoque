package com.controleestoque.controle.repository;

import com.controleestoque.controle.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    boolean existsByNome(String nome);
}
