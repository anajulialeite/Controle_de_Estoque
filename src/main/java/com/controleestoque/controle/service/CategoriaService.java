package com.controleestoque.controle.service;

import com.controleestoque.controle.model.Categoria;
import com.controleestoque.controle.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));
    }

    @Transactional
    public Categoria salvar(Categoria categoria) {
        if (categoria.getId() == null && categoriaRepository.existsByNome(categoria.getNome())) {
            throw new RuntimeException("Já existe uma categoria cadastrada com o nome: " + categoria.getNome());
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}
