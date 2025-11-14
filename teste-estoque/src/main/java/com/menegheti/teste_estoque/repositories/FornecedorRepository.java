package com.menegheti.teste_estoque.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.menegheti.teste_estoque.model.entities.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    
	Optional<Fornecedor> findByNome(String nome);
	Optional<Fornecedor> findByCnpj(String cnpj);
    
    boolean existsByCnpj(String cnpj);
    boolean existsByEmail(String email);
    
}
