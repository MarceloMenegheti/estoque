package com.menegheti.teste_estoque.model.exceptions;

public class UsuarioNaoAutorizadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UsuarioNaoAutorizadoException() {
        super();
    }
    
	public UsuarioNaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
    
    public UsuarioNaoAutorizadoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
