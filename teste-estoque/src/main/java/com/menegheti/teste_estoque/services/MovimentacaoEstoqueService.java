package com.menegheti.teste_estoque.services; 

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.menegheti.teste_estoque.model.DTO.MovimentacaoEstoqueDTO;
import com.menegheti.teste_estoque.model.entities.MovimentacaoEstoque;
import com.menegheti.teste_estoque.model.entities.Produto;
import com.menegheti.teste_estoque.model.entities.Usuario;
import com.menegheti.teste_estoque.model.enums.TipoMovimentacao;
import com.menegheti.teste_estoque.model.exceptions.EstoqueInsuficienteException;
import com.menegheti.teste_estoque.model.exceptions.MovimentacaoNaoEncontradaException;
import com.menegheti.teste_estoque.model.exceptions.ProdutoNaoEncontradoException;
import com.menegheti.teste_estoque.model.exceptions.UsuarioNaoEncontradoException;
import com.menegheti.teste_estoque.repositories.MovimentacaoEstoqueRepository;
import com.menegheti.teste_estoque.repositories.ProdutoRepository;
import com.menegheti.teste_estoque.repositories.UsuarioRepository;

import jakarta.validation.Valid;  


@Service  
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoRepository, 
                                      ProdutoRepository produtoRepository, 
                                      UsuarioRepository usuarioRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MovimentacaoEstoqueDTO salvar(MovimentacaoEstoqueDTO dto) {
        MovimentacaoEstoque movimentacao = fromDTO(dto);

        Long produtoId = movimentacao.getProduto().getId();
        if (produtoId == null) {
            throw new IllegalArgumentException("produtoId é obrigatório");
        }
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado: " + produtoId));

        Long usuarioId = movimentacao.getUsuario().getId();
        Usuario usuario = null;
        if (usuarioId != null) {
            usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + usuarioId));
        } else {
            throw new IllegalArgumentException("usuarioId é obrigatório");
        }

        // Validação de estoque SÓ para SAIDA
        if (movimentacao.getTipo() == TipoMovimentacao.SAIDA 
            && produto.getQuantidade() < movimentacao.getQuantidade()) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para " + produto.getNome() + "! Disponível: " + produto.getQuantidade());
        }

        // Switch para atualizar estoque
        int qtdAtual = produto.getQuantidade();
        int qtdMov = movimentacao.getQuantidade();
        switch (movimentacao.getTipo()) {
            case ENTRADA -> produto.setQuantidade(qtdAtual + qtdMov);
            case SAIDA -> produto.setQuantidade(qtdAtual - qtdMov);
        }

        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);

        produtoRepository.save(produto);
        MovimentacaoEstoque salvo = movimentacaoRepository.save(movimentacao);
        return toDTO(salvo);
    }

    private MovimentacaoEstoque fromDTO(MovimentacaoEstoqueDTO dto) {
        Produto produto = new Produto();
        if (dto.getProdutoId() != null) {
            produto.setId(dto.getProdutoId());
        }

        Usuario usuario = new Usuario();
        if (dto.getUsuarioId() != null) {
            usuario.setId(dto.getUsuarioId());
        }

        MovimentacaoEstoque m = new MovimentacaoEstoque();
        m.setId(dto.getId());
        m.setTipo(TipoMovimentacao.valueOf(dto.getTipo()));
        m.setQuantidade(dto.getQuantidade());
        m.setProduto(produto);
        m.setUsuario(usuario);
        m.setData(dto.getData() != null ? dto.getData() : LocalDateTime.now());
        return m;
    }

    @Transactional(readOnly = true)
    public Optional<MovimentacaoEstoqueDTO> buscarPorId(Long id) {
        return movimentacaoRepository.findById(id).map(this::toDTO);
    }

    @Transactional
    public void deletar(Long id) {
        Optional<MovimentacaoEstoque> optMov = movimentacaoRepository.findById(id);
        if (optMov.isEmpty()) {
            throw new RuntimeException("Movimentação não encontrada: " + id);
        }

        MovimentacaoEstoque mov = optMov.get();
        // Fetch produto se lazy (use query com JOIN FETCH se necessário)
        Produto produto = produtoRepository.findById(mov.getProduto().getId())
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado ao deletar"));

        // Reverta estoque
        int delta = (mov.getTipo() == TipoMovimentacao.SAIDA) ? +mov.getQuantidade() : -mov.getQuantidade();
        produto.setQuantidade(produto.getQuantidade() + delta);
        produtoRepository.save(produto);

        movimentacaoRepository.deleteById(id);
    }

    public MovimentacaoEstoqueDTO toDTO(MovimentacaoEstoque m) {
        return new MovimentacaoEstoqueDTO(
                m.getId(),
                m.getProduto() != null ? m.getProduto().getNome() : null,
                m.getUsuario() != null ? m.getUsuario().getNome() : null,  // Alinha com DTO usuarioNome
                m.getQuantidade(),
                m.getTipo() != null ? m.getTipo().name() : null,
                m.getData()
        );
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueDTO> listar() {
        return movimentacaoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueDTO> listarPorProduto(Long produtoId) {
        return movimentacaoRepository.findByProdutoId(produtoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueDTO> listarPorUsuario(Long usuarioId) {
        return movimentacaoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
	
    @Transactional
    public MovimentacaoEstoqueDTO atualizar(Long id, @Valid MovimentacaoEstoqueDTO dto) { // NOVO: @Valid para validar DTO (ex.: quantidade >0, tipo not blank)
        
        // Fetch existente
        MovimentacaoEstoque existente = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new MovimentacaoNaoEncontradaException("Movimentação não encontrada: " + id));  

        // Converte DTO para entity parcial
        MovimentacaoEstoque atualizada = fromDTO(dto);  

        // Validação básica no DTO convertido
        Long novoProdutoId = atualizada.getProduto().getId();
        if (novoProdutoId == null) {
            throw new IllegalArgumentException("produtoId é obrigatório para atualização");
        }
        
        Long novoUsuarioId = atualizada.getUsuario().getId();
        if (novoUsuarioId == null) {
            throw new IllegalArgumentException("usuarioId é obrigatório para atualização");
        }
        
        // Validação de tipo (evita NullPointer em valueOf)
        if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de movimentação é obrigatório para atualização");
        }
        atualizada.setTipo(TipoMovimentacao.valueOf(dto.getTipo().toUpperCase())); // UpperCase para segurança

        // Fetch novo produto e usuário
        Produto produtoNovo = produtoRepository.findById(novoProdutoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado: " + novoProdutoId));
        
        Usuario usuarioNovo = usuarioRepository.findById(novoUsuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + novoUsuarioId));

        // Reverta impacto do antigo (sempre, mesmo se produto/tipo não mudarem – garante net change correto)
        Long produtoIdAntigo = existente.getProduto().getId();
        Produto produtoAntigo = produtoRepository.findById(produtoIdAntigo)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto antigo não encontrado ao atualizar: " + produtoIdAntigo)); // FIX: Throw em vez de null
        
        int qtdAntiga = existente.getQuantidade();
        TipoMovimentacao tipoAntigo = existente.getTipo();
        int deltaAntigo = calcularDeltaEstoque(tipoAntigo, qtdAntiga);  
        // Reverta: Subtrai o delta antigo (inverte o impacto)
        produtoAntigo.setQuantidade(produtoAntigo.getQuantidade() - deltaAntigo);  
        produtoRepository.save(produtoAntigo);

        // Validação de estoque para o NOVO movimento 
        if (atualizada.getTipo() == TipoMovimentacao.SAIDA 
            && produtoNovo.getQuantidade() < atualizada.getQuantidade()) {
            throw new EstoqueInsuficienteException("Estoque insuficiente para " + produtoNovo.getNome() + "! Disponível: " + produtoNovo.getQuantidade());
        }

        // Aplique impacto do NOVO movimento no novo produto
        int qtdNova = atualizada.getQuantidade();
        TipoMovimentacao tipoNovo = atualizada.getTipo();
        int deltaNovo = calcularDeltaEstoque(tipoNovo, qtdNova);
        produtoNovo.setQuantidade(produtoNovo.getQuantidade() + deltaNovo); // + deltaNovo aplica o novo impacto
        produtoRepository.save(produtoNovo);

        // Atualiza a entity com ID preservado e referências
        atualizada.setId(id); 
        atualizada.setProduto(produtoNovo);
        atualizada.setUsuario(usuarioNovo);
        // Data: Use fornecida ou mantenha a original (melhor para audit: só mude se explicitamente fornecido)
        atualizada.setData(dto.getData() != null ? dto.getData() : existente.getData()); // FIX: Mantém data original se null
        
        MovimentacaoEstoque salva = movimentacaoRepository.save(atualizada);
        return toDTO(salva);
    }

    // Método auxiliar mantido (correto)
    private int calcularDeltaEstoque(TipoMovimentacao tipo, int quantidade) {
        return switch (tipo) {
            case ENTRADA -> +quantidade;  
            case SAIDA -> -quantidade; 
        };
    }
}

