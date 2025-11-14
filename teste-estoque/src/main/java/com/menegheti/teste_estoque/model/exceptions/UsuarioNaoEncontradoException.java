package com.menegheti.teste_estoque.model.exceptions;

import java.io.Serial;

public class UsuarioNaoEncontradoException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    
    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado.");
    }
    
    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    
    public UsuarioNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
    
    public UsuarioNaoEncontradoException(Throwable causa) {
        super(causa);
    }
}