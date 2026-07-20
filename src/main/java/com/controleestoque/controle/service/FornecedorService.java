package com.controleestoque.controle.service;

import com.controleestoque.controle.model.Fornecedor;
import com.controleestoque.controle.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    @Autowired
    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional(readOnly = true)
    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado com o ID: " + id));
    }

    @Transactional
    public Fornecedor salvar(Fornecedor fornecedor) {
        // Valida CNPJ apenas no cadastro de um novo fornecedor
        if (fornecedor.getId() == null && fornecedor.getCnpj() != null && 
            fornecedorRepository.existsByCnpj(fornecedor.getCnpj())) {
            throw new RuntimeException("Já existe um fornecedor cadastrado com o CNPJ: " + fornecedor.getCnpj());
        }
        return fornecedorRepository.save(fornecedor);
    }

    @Transactional
    public void excluir(Long id) {
        Fornecedor fornecedor = buscarPorId(id);
        fornecedorRepository.delete(fornecedor);
    }
}
