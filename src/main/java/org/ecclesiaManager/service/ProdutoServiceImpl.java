package org.ecclesiaManager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ecclesiaManager.model.Igreja;
import org.ecclesiaManager.model.Produto;
import org.ecclesiaManager.model.dto.ProdutoRequestDTO;
import org.ecclesiaManager.repository.IgrejaRepository;
import org.ecclesiaManager.repository.ProdutoRepository;

import java.util.List;

@ApplicationScoped
public class ProdutoServiceImpl implements IProdutoService {

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    IgrejaRepository igrejaRepository;

    @Override
    @Transactional
    public Produto criar(Long igrejaId, ProdutoRequestDTO dto) {
        // No Panache, usamos findByIdOptional
        Igreja igreja = igrejaRepository.findByIdOptional(igrejaId)
                .orElseThrow(() -> new RuntimeException("Igreja não encontrada"));

        Produto produto = new Produto();
        // Acesso aos atributos do Record (sem o prefixo 'get')
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());
        produto.setImageUrl(dto.imageUrl());
        produto.setAtivo(dto.ativo() != null ? dto.ativo() : true);
        produto.setIgreja(igreja);

        // No Panache usamos persist em vez de save
        produtoRepository.persist(produto);
        return produto;
    }

    @Override
    @Transactional
    public Produto atualizar(Long igrejaId, Long id, ProdutoRequestDTO dto) {
        Produto produto = buscarPorId(igrejaId, id);

        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());
        produto.setImageUrl(dto.imageUrl());

        if (dto.ativo() != null) {
            produto.setAtivo(dto.ativo());
        }

        // Como o produto já está "managed" pela transação, o Hibernate atualiza
        // automaticamente na base de dados, mas chamamos o persist por clareza.
        produtoRepository.persist(produto);
        return produto;
    }

    @Override
    public List<Produto> listar(Long igrejaId) {
        // Usa o método customizado que criámos no ProdutoRepository
        return produtoRepository.findAllByIgrejaId(igrejaId);
    }

    @Override
    public Produto buscarPorId(Long igrejaId, Long id) {
        Produto produto = produtoRepository.findByIdOptional(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Garantir que o produto pertence de facto à igreja que o está a solicitar
        if (!produto.getIgreja().getId().equals(igrejaId)) {
            throw new RuntimeException("Produto não pertence a esta igreja");
        }

        return produto;
    }
}