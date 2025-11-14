package com.menegheti.teste_estoque.controllers;  // Ajuste pacote

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.menegheti.teste_estoque.model.DTO.UsuarioDTO;
import com.menegheti.teste_estoque.model.entities.Usuario;
import com.menegheti.teste_estoque.model.exceptions.EmailJaCadastradoException;
import com.menegheti.teste_estoque.model.exceptions.UsuarioNaoEncontradoException;
import com.menegheti.teste_estoque.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Validated
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  

    @PostMapping("/cadastro")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioDTO dto) {
        try {
            UsuarioDTO novo = usuarioService.salvar(dto);  
            return ResponseEntity.status(HttpStatus.CREATED).body(novo);
        } catch (EmailJaCadastradoException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno no cadastro: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");

        if (email == null || senha == null || email.trim().isEmpty() || senha.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email e senha obrigatórios"));
        }

        try {
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + email));

            if (!passwordEncoder.matches(senha, usuario.getSenhaHash())) {
                return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
            }

            // ✅ Resposta completa com ID e dados do usuário
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("message", "Login realizado com sucesso!");
            resposta.put("id", usuario.getId());
            resposta.put("nome", usuario.getNome());
            resposta.put("email", usuario.getEmail());
            resposta.put("perfil", usuario.getPerfil().name());

            return ResponseEntity.ok(resposta);

        } catch (UsuarioNaoEncontradoException | IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro interno no login"));
        }
    }


    @GetMapping("/perfil")
    public ResponseEntity<Map<String, String>> getPerfilLogado() {
        return ResponseEntity.ok(Map.of(
                "message", "Perfil endpoint (sem security ativa)",
                "perfil", "TESTE"
        ));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listar();
        return ResponseEntity.ok(usuarios);
    }
}
