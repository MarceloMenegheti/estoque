package com.menegheti.teste_estoque.model.exceptions;

public class ProdutoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    public ProdutoNaoEncontradoException(String message) {
        super(message);  // Chama o construtor da RuntimeException com a mensagem
    }
    
    public ProdutoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);  // Para casos com causa raiz (ex.: erro no banco)
    }


}