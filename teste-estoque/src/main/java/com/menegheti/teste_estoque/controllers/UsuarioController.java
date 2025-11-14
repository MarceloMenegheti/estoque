package com.menegheti.teste_estoque.controllers;  // Ajuste pacote

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.menegheti.teste_estoque.model.DTO.UsuarioDTO;
import com.menegheti.teste_estoque.model.exceptions.EmailJaCadastradoException;
import com.menegheti.teste_estoque.model.exceptions.UsuarioNaoEncontradoException;
import com.menegheti.teste_estoque.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Validated
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<UsuarioDTO> usuarios = usuarioService.listar();
        return ResponseEntity.ok(usuarios);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<UsuarioDTO>> buscarPorNome(@PathVariable String nome) {
        List<UsuarioDTO> usuarios = usuarioService.buscarPorNome(nome);
        return ResponseEntity.ok(usuarios);
    }
   
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody UsuarioDTO dto) {
        try {
            UsuarioDTO novo = usuarioService.salvar(dto);  // Chama criar (hash automático)
            return ResponseEntity.status(HttpStatus.CREATED).body(novo);
        } catch (EmailJaCadastradoException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(new ErroResponse("Erro no banco: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErroResponse("Erro interno: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        try {
            // Opcional: Set ID no DTO se precisar (mas service usa path id)
            dto.setId(id);  // Para consistência, mas não obrigatório
            UsuarioDTO atualizado = usuarioService.atualizar(dto, id);
            return ResponseEntity.ok(atualizado);
        } catch (UsuarioNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (EmailJaCadastradoException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErroResponse(e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResponse("Violação de integridade no banco: " + e.getMessage()));  // Seu 409
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErroResponse("Erro interno: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!usuarioService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
    
    @GetMapping("/total")
    public ResponseEntity<Long> getTotalUsuarios() {
        long total = usuarioService.contarTotalUsuarios();
        return ResponseEntity.ok(total);
    }
    
    // Classe auxiliar para erros 
    public static class ErroResponse {
        private String message;
        public ErroResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}