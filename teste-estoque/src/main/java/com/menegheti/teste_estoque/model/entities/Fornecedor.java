package com.menegheti.teste_estoque.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "fornecedores")
public class Fornecedor implements Serializable{

	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cnpj", nullable = false, unique = true, length = 20)
    private String cnpj;

    @Column(name = "email", unique = true, length = 120)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();
    
    public Fornecedor() {}

	public Fornecedor(Long id, String nome, String cnpj, String email, String telefone) {
		super();
		this.id = id;
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.cnpj = cnpj;
	}

	public Long getId() {return id;}

	public void setId(Long id) {this.id = id;}

	public String getNome() {return nome;}

	public void setNome(String nome) {this.nome = nome;}

	public String getCnpj() {return cnpj;}

	public void setCnpj(String cnpj) {this.cnpj = cnpj;}

	public String getEmail() {return email;}

	public void setEmail(String email) {this.email = email;}

	public String getTelefone() {return telefone;}

	public void setTelefone(String telefone) {this.telefone = telefone;}

    public List<Produto> getProdutos() { return produtos; }
    
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {return "FornecedorDTO [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email + "]";}
    
}
