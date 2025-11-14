package com.menegheti.teste_estoque.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.menegheti.teste_estoque.model.DTO.ProdutoDTO;
import com.menegheti.teste_estoque.model.entities.Fornecedor;
import com.menegheti.teste_estoque.model.entities.Produto;
import com.menegheti.teste_estoque.model.enums.Categoria;
import com.menegheti.teste_estoque.model.exceptions.CategoriaInvalidaException;
import com.menegheti.teste_estoque.model.exceptions.FornecedorNaoEncontradoException;
import com.menegheti.teste_estoque.model.exceptions.ProdutoNaoEncontradoException;
import com.menegheti.teste_estoque.repositories.FornecedorRepository;
import com.menegheti.teste_estoque.repositories.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private FornecedorRepository fornecedorRepository;

    public ProdutoService(ProdutoRepository produtoRepository, FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional
    public ProdutoDTO salvar(ProdutoDTO dto) {
        Produto produto = fromDTO(dto);
        Produto salvo = produtoRepository.save(produto);
        return toDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listar() {
        return produtoRepository.findAllWithFornecedor()  // Carrega fornecedor para evitar NPE
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProdutoDTO> buscarPorId(Long id) {
        return produtoRepository.findByIdWithFornecedor(id)  // Carrega fornecedor
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarPorFornecedorId(Long fornecedorId) {
        return produtoRepository.findByFornecedorIdWithFetch(fornecedorId)  // Assuma método com fetch no repo
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarPorCategoria(String categoriaStr) {
        try {
            Categoria categoria = Categoria.valueOf(categoriaStr.toUpperCase());
            return produtoRepository.findAllWithFornecedor()  // Carrega fornecedor
                    .stream()
                    .filter(p -> p.getCategoria() == categoria)
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new CategoriaInvalidaException("Categoria inválida: " + categoriaStr + ". Opções válidas: ELETRONICOS, ROUPAS, LIVROS, ALIMENTOS.");
        }
    }

    @Transactional(readOnly = true)
    public List<Fornecedor> listarFornecedores() {
        return fornecedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Fornecedor> buscarFornecedorPorId(Long id) {
        return fornecedorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Fornecedor> buscarFornecedorPorNome(String nome) {  // Corrigi o nome do método para consistência
        return fornecedorRepository.findByNome(nome);
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
        return produtoRepository.findById(id)
                .map(entidade -> {
                    entidade.setNome(dto.getNome());
                    entidade.setDescricao(dto.getDescricao());
                    entidade.setQuantidade(dto.getQuantidade());
                    entidade.setPreco(dto.getPreco());
                    entidade.setFornecedor(fornecedorRepository.findById(dto.getFornecedorId())
                            .orElseThrow(() -> new FornecedorNaoEncontradoException("Fornecedor não encontrado: " + dto.getFornecedorId())));
                    try {
                        entidade.setCategoria(Categoria.valueOf(dto.getCategoria().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        throw new CategoriaInvalidaException("Categoria inválida: " + dto.getCategoria());
                    }
                    Produto atualizado = produtoRepository.save(entidade);
                    return toDTO(atualizado);
                })
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com ID: " + id));
    }

    @Transactional
    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> listarComPaginacao(Pageable pageable) {
       
        return produtoRepository.findAll(pageable)
                .map(this::toDTO);
    }
    
    private ProdutoDTO toDTO(Produto p) {
        Long fornecedorId = null;
        String fornecedorNome = "N/A";
        if (p.getFornecedor() != null) {
            fornecedorId = p.getFornecedor().getId();
            fornecedorNome = p.getFornecedor().getNome();
        }
        String categoriaStr = (p.getCategoria() != null) ? p.getCategoria().name() : null;
        if (categoriaStr == null) {
            throw new CategoriaInvalidaException("Categoria não definida para o produto ID: " + p.getId());
        }
        return new ProdutoDTO(
                p.getId(),
                p.getNome(),
                p.getDescricao(),
                p.getQuantidade(),
                p.getPreco(),
                fornecedorId,
                categoriaStr,
                fornecedorNome  
        );
    }

    private Produto fromDTO(ProdutoDTO dto) {
        Produto p = new Produto();
        
        if (dto.getId() != null) p.setId(dto.getId());
        p.setNome(dto.getNome());
        p.setDescricao(dto.getDescricao());
        p.setQuantidade(dto.getQuantidade());
        p.setPreco(dto.getPreco());
        
        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new FornecedorNaoEncontradoException("Fornecedor não encontrado com ID: " + dto.getFornecedorId()));
        p.setFornecedor(fornecedor);
        try {
            p.setCategoria(Categoria.valueOf(dto.getCategoria().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new CategoriaInvalidaException("Categoria inválida: " + dto.getCategoria() + ". Opções: ELETRONICOS, ROUPAS, LIVROS, ALIMENTOS.");
        }
        return p;
    }
    
    @Transactional(readOnly = true)
    public long contarTotalProdutos() {
        return produtoRepository.count();
    }
    
    @Transactional(readOnly = true)
    public long contarTotalItensEstoque() {
        return produtoRepository.calcularTotalItensEstoque();
    }
}
