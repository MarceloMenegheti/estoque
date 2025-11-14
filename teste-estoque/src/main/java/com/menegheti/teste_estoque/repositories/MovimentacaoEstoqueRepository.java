package com.menegheti.teste_estoque.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.menegheti.teste_estoque.model.entities.MovimentacaoEstoque;
import com.menegheti.teste_estoque.model.enums.TipoMovimentacao;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    
	// Histórico por produto (JOIN implícito via relação)
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.produto.id = :produtoId ORDER BY m.data DESC")
    List<MovimentacaoEstoque> findByProdutoId(@Param("produtoId") Long produtoId);
    
    // Histórico por usuário
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.usuario.id = :usuarioId ORDER BY m.data DESC")
    List<MovimentacaoEstoque> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    // Opcional: Por tipo e data (para relatórios)
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.tipo = :tipo AND m.data >= :dataInicio ORDER BY m.data DESC")
    List<MovimentacaoEstoque> findByTipoAndDataAfter(@Param("tipo") TipoMovimentacao tipo, @Param("dataInicio") LocalDateTime dataInicio);
}