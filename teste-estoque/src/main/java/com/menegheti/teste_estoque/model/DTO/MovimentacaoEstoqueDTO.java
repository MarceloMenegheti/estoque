package com.menegheti.teste_estoque.model.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MovimentacaoEstoqueDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("produtoNome")
    private String produtoNome;  
    @JsonProperty("usuarioNome")
    private String usuarioNome;  
    
    
    @NotNull(message = "produtoId é obrigatório")
    private Long produtoId;
    
    @NotNull(message = "usuarioId é obrigatório")
    private Long usuarioId;
    
    @NotNull
    @Min(value = 1, message = "Quantidade deve ser maior que 0")
    private int quantidade;
    
    @NotBlank(message = "Tipo é obrigatório")
    private String tipo;
    
    private LocalDateTime data;
    
    public MovimentacaoEstoqueDTO() {}

    public MovimentacaoEstoqueDTO(Long id, String produtoNome, String usuarioNome, int quantidade, String tipo, LocalDateTime data) {
      
        this.id = id;
        this.produtoNome = produtoNome;
        this.usuarioNome = usuarioNome;
        this.produtoId = null; 
        this.usuarioId = null;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.data = data;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    
    public String getProdutoNome() {return produtoNome;}
    public void setProdutoNome(String produtoNome) {this.produtoNome = produtoNome;}
    
    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}
    
    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    
    public LocalDateTime getData() {return data;}
    public void setData(LocalDateTime data) {this.data = data;}
    
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public String getUsuarioNome() { return usuarioNome;}
    public void setUsuarioNome(String usuarioNome) {this.usuarioNome = usuarioNome;}
    }
