package com.menegheti.teste_estoque.model.exceptions;

public class MovimentacaoNaoEncontradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MovimentacaoNaoEncontradaException(String message) {
        super(message);
    }
}
