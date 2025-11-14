package com.menegheti.teste_estoque.model.DTO;

import java.math.BigDecimal;
import java.util.Objects;

public class ProdutoDTO {

	   	private Long id;
	    private String nome;
	    private String descricao;
	    private int quantidade;
	    private BigDecimal preco;
	    
	    private Long fornecedorId; 
	    
	    private String fornecedorNome;
	    
	    private String categoria;
	    	    
	    public ProdutoDTO() {}

	    public ProdutoDTO(Long id, String nome, String descricao, Integer quantidade, BigDecimal preco, Long fornecedorId, String categoria, String fornecedorNome) {
			  this.id = id;
			  this.nome = nome;
			  this.descricao = descricao;
			  this.quantidade = quantidade;
			  this.preco = preco;
			  this.fornecedorId = fornecedorId;
			  this.categoria = categoria;
			  this.fornecedorNome = fornecedorNome;
			}

		public Long getId() {return id;}
		public void setId(Long id) {this.id = id;}

		public String getNome() {return nome;}
		public void setNome(String nome) {this.nome = nome;}

		public String getDescricao() {return descricao;}
		public void setDescricao(String descricao) {this.descricao = descricao;}

		public int getQuantidade() {return quantidade;}
		public void setQuantidade(int quantidade) {this.quantidade = quantidade;}

		public BigDecimal getPreco() {return preco;}
		public void setPreco(BigDecimal preco) {this.preco = preco;}

		public Long getFornecedorId() {return fornecedorId;}
		public void setFornecedorId(Long fornecedorId) {this.fornecedorId = fornecedorId;}

		public String getCategoria() {return categoria;}
		public void setCategoria(String categoria) {this.categoria = categoria;}
		
		public String getFornecedorNome() {return fornecedorNome;}
		public void setFornecedorNome(String fornecedorNome) {this.fornecedorNome = fornecedorNome;}

		@Override
		public int hashCode() {return Objects.hash(id);}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProdutoDTO other = (ProdutoDTO) obj;
			return Objects.equals(id, other.id);
		}

		@Override
		public String toString() {
			return "ProdutoDTO [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", quantidade=" + quantidade
					+ ", preco=" + preco + ", fornecedorId=" + fornecedorId + ", categoria="
					+ categoria + "]";
		}
}
