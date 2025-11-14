package com.menegheti.teste_estoque.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.menegheti.teste_estoque.model.DTO.FornecedorDTO;
import com.menegheti.teste_estoque.model.entities.Fornecedor;
import com.menegheti.teste_estoque.services.FornecedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fornecedores")
@CrossOrigin(origins = "*")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    
    @PostMapping
    public ResponseEntity<FornecedorDTO> criar(@Valid @RequestBody FornecedorDTO dto) {  // Mude para DTO
        Fornecedor novo = fornecedorService.fromDTO(dto);  // Mapeia DTO → Entidade
        Fornecedor salvo = fornecedorService.salvar(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.toDTO(salvo));
    }
    
    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> listar() {
        return ResponseEntity.ok(fornecedorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDTO> buscarPorId(@PathVariable Long id) {
        Optional<FornecedorDTO> dto = fornecedorService.buscarPorId(id);
        if (dto.isPresent()) {
            return ResponseEntity.ok(dto.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorDTO> atualizar(@PathVariable Long id, @Valid @RequestBody FornecedorDTO dtoAtualizado) {
        Optional<Fornecedor> fornecedorExistenteOpt = fornecedorService.buscarEntidadePorId(id);
        if (fornecedorExistenteOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado");
        }
        Fornecedor fornecedorExistente = fornecedorExistenteOpt.get();
        
        // Usa o método do service para cópia segura (cnpj/nome garantidos não-null pelo @Valid)
        fornecedorService.atualizarFromDTO(fornecedorExistente, dtoAtualizado);
        
        Fornecedor fornecedorSalvo = fornecedorService.salvar(fornecedorExistente);
        return ResponseEntity.ok(fornecedorService.toDTO(fornecedorSalvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (fornecedorService.buscarEntidadePorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor não encontrado");
        }
        fornecedorService.deletar(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
