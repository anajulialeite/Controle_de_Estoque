package com.controleestoque.controle.repository;

import com.controleestoque.controle.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    
    boolean existsByCnpj(String cnpj);
}
