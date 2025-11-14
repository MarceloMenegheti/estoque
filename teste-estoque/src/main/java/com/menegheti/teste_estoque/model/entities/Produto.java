package com.menegheti.teste_estoque.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import com.menegheti.teste_estoque.model.enums.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "produtos")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    @NotBlank(message = "Nome não pode ser vazio")
    private String nome;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "quantidade", nullable = false)
    @Min(value = 0, message = "Quantidade deve ser positiva")
    private int quantidade;

    @Column(name = "preco", precision = 10, scale = 2,nullable = false)
    private BigDecimal preco = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    @NotNull(message = "Fornecedor é obrigatório")
    private Fornecedor fornecedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 30)
    @NotNull(message = "Categoria é obrigatória")
    private Categoria categoria;

    public Produto() {}

    public Produto(Long id, String nome, String descricao, int quantidade, BigDecimal preco, Fornecedor fornecedor, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
        this.fornecedor = fornecedor;
        this.categoria = categoria;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Fornecedor getFornecedor() { return fornecedor; }  
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public Categoria getCategoria() { return categoria; } 
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produto other = (Produto) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "Produto [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", quantidade=" + quantidade
                + ", preco=" + preco +  ", fornecedor=" + (fornecedor != null ? fornecedor.getNome() : null)
                + ", categoria=" + categoria + "]";
    }
}
