package com.menegheti.teste_estoque.repositories;

import java.util.List;   
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.menegheti.teste_estoque.model.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Optional<Usuario> findByEmail(String email);
	Stream<Usuario> findByNome(String nome);
	List<Usuario> findByNomeContainingIgnoreCase(String nome);
	boolean existsByEmail(String email);
	boolean existsByEmailAndId(String email, Long id);
	boolean existsByEmailAndIdNot(String email, Long id);
    
}