package com.menegheti.teste_estoque.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.menegheti.teste_estoque.model.DTO.MovimentacaoEstoqueDTO;
import com.menegheti.teste_estoque.services.MovimentacaoEstoqueService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movimentacoes")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})  
public class MovimentacaoEstoqueController {
    private final MovimentacaoEstoqueService movimentacaoService;
    
    public MovimentacaoEstoqueController(MovimentacaoEstoqueService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }
   
    @PostMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'ESTOQUISTA')")
    public ResponseEntity<MovimentacaoEstoqueDTO> criar(@Valid @RequestBody MovimentacaoEstoqueDTO dto) {
        MovimentacaoEstoqueDTO dtoSalvo = movimentacaoService.salvar(dto);  
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvo);  
    }
    
    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'ESTOQUISTA')")
    public ResponseEntity<List<MovimentacaoEstoqueDTO>> listar() {
        return ResponseEntity.ok(movimentacaoService.listar());
    }
    
    @GetMapping("/produto/{produtoId}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'ESTOQUISTA')")
    public ResponseEntity<List<MovimentacaoEstoqueDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(movimentacaoService.listarPorProduto(produtoId));
    }
    
    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'ESTOQUISTA')")
    public ResponseEntity<MovimentacaoEstoqueDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MovimentacaoEstoqueDTO dto) {
        MovimentacaoEstoqueDTO dtoAtualizado = movimentacaoService.atualizar(id, dto);
        return ResponseEntity.ok(dtoAtualizado);  
    }
}
