package com.menegheti.teste_estoque.model.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardResumoDTO {
    private int totalFornecedores;
    private int totalProdutos;  
    private double valorTotalEstoque; 

    // Para gráfico pizza: Produtos por Fornecedor (Map<nomeFornecedor, countProdutos>)
    private Map<String, Integer> produtosPorFornecedor = new HashMap<>();

    // Para segundo gráfico: Produtos por Categoria + Fornecedor
    private List<CategoriaFornecedorResumo> produtosPorCategoriaFornecedor = new ArrayList<>();

    // Inner class para o segundo gráfico
    public static class CategoriaFornecedorResumo {
        private String categoria;
        private String fornecedorNome;
        private int quantidadeProdutos;

        public CategoriaFornecedorResumo() {}

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        public String getFornecedorNome() { return fornecedorNome; }
        public void setFornecedorNome(String fornecedorNome) { this.fornecedorNome = fornecedorNome; }

        public int getQuantidadeProdutos() { return quantidadeProdutos; }
        public void setQuantidadeProdutos(int quantidadeProdutos) { this.quantidadeProdutos = quantidadeProdutos; }
    }

    public DashboardResumoDTO() {}

    public int getTotalFornecedores() { return totalFornecedores; }
    public void setTotalFornecedores(int totalFornecedores) { this.totalFornecedores = totalFornecedores; }

    // NOVO: Getters/Setters para totalProdutos
    public int getTotalProdutos() { return totalProdutos; }
    public void setTotalProdutos(int totalProdutos) { this.totalProdutos = totalProdutos; }

    public double getValorTotalEstoque() { return valorTotalEstoque; }
    public void setValorTotalEstoque(double valorTotalEstoque) { this.valorTotalEstoque = valorTotalEstoque; }

    public Map<String, Integer> getProdutosPorFornecedor() { return produtosPorFornecedor; }
    public void setProdutosPorFornecedor(Map<String, Integer> produtosPorFornecedor) { this.produtosPorFornecedor = produtosPorFornecedor; }

    public List<CategoriaFornecedorResumo> getProdutosPorCategoriaFornecedor() { return produtosPorCategoriaFornecedor; }
    public void setProdutosPorCategoriaFornecedor(List<CategoriaFornecedorResumo> produtosPorCategoriaFornecedor) { this.produtosPorCategoriaFornecedor = produtosPorCategoriaFornecedor; }
}
