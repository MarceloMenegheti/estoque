package com.menegheti.teste_estoque.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.menegheti.teste_estoque.model.entities.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{

	    Optional<Produto> findByNome(String nome); 
	    
	    Optional<Produto> findByCategoria(String Categoria);
	    
	    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.fornecedor")
	    List<Produto> findAllWithFornecedor();
	    
	    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.fornecedor WHERE p.id = :id")
	    Optional<Produto> findByIdWithFornecedor(@Param("id") Long id);
	    
	    // Para outros métodos, adicione fetch se necessário
	    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.fornecedor WHERE p.fornecedor.id = :fornecedorId")
	    List<Produto> findByFornecedorIdWithFetch(@Param("fornecedorId") Long fornecedorId);

	    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.fornecedor WHERE p.fornecedor.id = :id")
	    List<Produto> findByFornecedorId(@Param("id") Long id);

	    // Produtos com estoque crítico (baixo estoque)
	    @Query("SELECT p FROM Produto p WHERE p.quantidade < :limiteEstoque ORDER BY p.quantidade ASC")
	    List<Produto> findByQuantidadeEstoqueLessThan(@Param("limiteEstoque") int limiteEstoque);
	    
	    @Query("SELECT COALESCE(SUM(p.quantidade * p.preco), 0) FROM Produto p")
	    BigDecimal calcularValorTotalEstoque();
	    
	    // 2. Produtos por Fornecedor: Mantém JPQL (funciona sem enum)
	    @Query("SELECT f.nome, COUNT(p) FROM Produto p LEFT JOIN p.fornecedor f GROUP BY f.id, f.nome")
	    List<Object[]> findProdutosPorFornecedor();
	    	    
	    @Query(value ="SELECT p.categoria, f.nome, COUNT(p.id) FROM produtos p LEFT JOIN fornecedores f ON p.fornecedor_id = f.id " +
	    	    "GROUP BY p.categoria, f.id, f.nome", nativeQuery = true)
	    List<Object[]> findProdutosPorCategoriaFornecedor();
	    
	    @Query("SELECT COALESCE(SUM(p.quantidade), 0L) FROM Produto p")
	    long calcularTotalItensEstoque();

}