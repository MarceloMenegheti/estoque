package com.menegheti.teste_estoque.model.DTO;

import com.menegheti.teste_estoque.model.enums.Perfil;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class UsuarioDTO {
	
    private Long id;
    private String nome;
    private String email;
    private String senha;
    
    @Enumerated(EnumType.STRING)
    private Perfil perfil; 

    public UsuarioDTO() {}

    public UsuarioDTO(Long id, String nome, String email, String senha ,Perfil perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.senha = senha;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}
    
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
      
    public String getSenha() {return senha;}
	public void setSenha(String senha) {this.senha = senha;}

	public Perfil getPerfil() {return perfil;}
    public void setPerfil(Perfil perfil) {this.perfil = perfil;}
}
