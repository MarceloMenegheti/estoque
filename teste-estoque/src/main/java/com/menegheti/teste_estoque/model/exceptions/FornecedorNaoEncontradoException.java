package com.menegheti.teste_estoque.model.exceptions;

public class FornecedorNaoEncontradoException  extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public FornecedorNaoEncontradoException() {
	    super();
	}
	
	public FornecedorNaoEncontradoException(String mensagem) {
	    super(mensagem);
	}
	
	public FornecedorNaoEncontradoException(String mensagem, Throwable causa) {
	    super(mensagem, causa);
	}
}
