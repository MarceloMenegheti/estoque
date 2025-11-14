package com.menegheti.teste_estoque.model.exceptions;

public class EstoqueInsuficienteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EstoqueInsuficienteException() {
        super();
    }
    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
    public EstoqueInsuficienteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}