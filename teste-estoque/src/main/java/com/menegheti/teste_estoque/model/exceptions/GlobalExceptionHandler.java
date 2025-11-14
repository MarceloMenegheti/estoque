package com.menegheti.teste_estoque.model.exceptions;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<?> handleProdutoNaoEncontrado(ProdutoNaoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Map.of("erro", e.getMessage()));
    }
	
    @ExceptionHandler(FornecedorNaoEncontradoException.class)
    public ResponseEntity<?> handleFornecedorNaoEncontrado(FornecedorNaoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Map.of("erro", e.getMessage()));
    }
    
    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<?> handleEstoqueInsuficiente(EstoqueInsuficienteException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(CategoriaInvalidaException.class)
    public ResponseEntity<?> handleCategoriaInvalida(CategoriaInvalidaException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(Map.of("erro", e.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        // {"cnpj": "CNPJ é obrigatório"})
        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        Map<String, String> error = new HashMap<>();
        String message = "Violação de integridade no banco: ";
        if (e.getMessage() != null && (e.getMessage().contains("cnpj") || e.getMessage().contains("Unique") || e.getMessage().contains("duplicate"))) {
            message += "CNPJ já cadastrado ou inválido";
        } else if (e.getMessage() != null && e.getMessage().contains("null")) {
            message += "Campo obrigatório não pode ser nulo";
        } else {
            message += "Dados inválidos";
        }
        error.put("erro", message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);  // 409 para unique; use BAD_REQUEST (400) se preferir
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, String>> handleNullPointer(NullPointerException e) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro interno: Objeto não inicializado. Contate o suporte. Detalhes: " + e.getMessage());
        e.printStackTrace();  // Log para depuração
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", e.getMessage());  // Ex.: "Apenas arquivos de imagem são permitidos"
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    // Para Exception no upload de arquivos
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handleIOException(IOException e) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro ao processar arquivo: " + e.getMessage());
        e.printStackTrace();  // Log
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    // Fallback para RuntimeException genérica no upload
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Erro inesperado no servidor: " + e.getMessage());
        e.printStackTrace();  // Log sempre
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
