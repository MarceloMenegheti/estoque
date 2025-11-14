package com.menegheti.teste_estoque.model.exceptions;

public class CategoriaInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CategoriaInvalidaException() {
        super();
    }
    
	public CategoriaInvalidaException(String mensagem) {
        super(mensagem);
    }
    
    public CategoriaInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
