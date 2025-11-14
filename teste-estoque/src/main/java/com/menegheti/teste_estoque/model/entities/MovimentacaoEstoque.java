package com.menegheti.teste_estoque.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.menegheti.teste_estoque.model.enums.TipoMovimentacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) 
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoMovimentacao tipo;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime data;
    
    
    public MovimentacaoEstoque() {}
    
	public MovimentacaoEstoque(Long id, TipoMovimentacao tipo, int quantidade, Usuario usuario, Produto produto,
			LocalDateTime data) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.quantidade = quantidade;
		this.usuario = usuario;
		this.produto = produto;
		this.data = data;
	}
	
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public TipoMovimentacao getTipo() { return tipo; }
    public void setTipo(TipoMovimentacao tipo) { this.tipo = tipo; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

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
		MovimentacaoEstoque other = (MovimentacaoEstoque) obj;
		return Objects.equals(id, other.id);
	}
    
}
