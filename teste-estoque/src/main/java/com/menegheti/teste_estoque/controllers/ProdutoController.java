package com.menegheti.teste_estoque.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.menegheti.teste_estoque.model.DTO.ProdutoDTO;
import com.menegheti.teste_estoque.services.ProdutoService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Validated
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalProdutos() {
        long total = produtoService.contarTotalProdutos();
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/total-itens")
    public ResponseEntity<Long> getTotalItensEstoque() {
        long totalItens = produtoService.contarTotalItensEstoque();
        return ResponseEntity.ok(totalItens);
    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody ProdutoDTO dto) {
        try {
            ProdutoDTO salvo = produtoService.salvar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Erro ao salvar produto", "detalhe", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listar() {
        List<ProdutoDTO> lista = produtoService.listar();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        Optional<ProdutoDTO> produto = produtoService.buscarPorId(id);
        return produto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<ProdutoDTO>> listarPorFornecedorId(@PathVariable Long fornecedorId) {
        List<ProdutoDTO> lista = produtoService.listarPorFornecedorId(fornecedorId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProdutoDTO>> listarPorCategoria(@PathVariable String categoria) {
        List<ProdutoDTO> lista = produtoService.listarPorCategoria(categoria);
        return ResponseEntity.ok(lista);
    }

    @PutMapping(value = "/{id}")  // ← ADICIONE ISSO
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        ProdutoDTO atualizado = produtoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
