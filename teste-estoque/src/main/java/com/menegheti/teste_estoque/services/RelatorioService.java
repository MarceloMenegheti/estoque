package com.menegheti.teste_estoque.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.menegheti.teste_estoque.model.DTO.DashboardResumoDTO;
import com.menegheti.teste_estoque.model.entities.Fornecedor;
import com.menegheti.teste_estoque.repositories.FornecedorRepository;
import com.menegheti.teste_estoque.repositories.ProdutoRepository;

@Service
@Transactional(readOnly = true)
public class RelatorioService {

    @Autowired
    private ProdutoRepository produtoRepository;  

    @Autowired
    private FornecedorRepository fornecedorRepository;  

    public DashboardResumoDTO gerarDashboard() {
        DashboardResumoDTO dto = new DashboardResumoDTO();

        // 1. Total Fornecedores (simples)
        dto.setTotalFornecedores((int) fornecedorRepository.count());

        // 2. Total Produtos (simples)
        dto.setTotalProdutos((int) produtoRepository.count());

        // 3. Produtos por Fornecedor (chama repository + mapeia para Map)
        List<Object[]> resultadosFornecedor = produtoRepository.findProdutosPorFornecedor();
        Map<String, Integer> produtosPorFornecedor = resultadosFornecedor.stream()
            .collect(Collectors.toMap(
                result -> (String) result[0],  // nomeFornecedor
                result -> ((Long) result[1]).intValue()  // count
            ));
        // Adicione fornecedores sem produtos
        List<Fornecedor> todosFornecedores = fornecedorRepository.findAll();
        todosFornecedores.forEach(f -> {
            if (!produtosPorFornecedor.containsKey(f.getNome())) {
                produtosPorFornecedor.put(f.getNome(), 0);
            }
        });
        dto.setProdutosPorFornecedor(produtosPorFornecedor);

        // 4. Produtos por Categoria + Fornecedor (chama repository + mapeia para List)
        List<Object[]> resultadosCategoria = produtoRepository.findProdutosPorCategoriaFornecedor();
        List<DashboardResumoDTO.CategoriaFornecedorResumo> resumos = resultadosCategoria.stream().map(result -> {
            DashboardResumoDTO.CategoriaFornecedorResumo resumo = new DashboardResumoDTO.CategoriaFornecedorResumo();
            resumo.setCategoria((String) result[0]);  // String do enum.name()
            resumo.setFornecedorNome((String) result[1]);
            resumo.setQuantidadeProdutos(((Long) result[2]).intValue());
            return resumo;
        }).collect(Collectors.toList());
        dto.setProdutosPorCategoriaFornecedor(resumos);

        // 5. Valor Total do Estoque (chama repository direto)
        BigDecimal totalBig = produtoRepository.calcularValorTotalEstoque();
        dto.setValorTotalEstoque(totalBig.doubleValue());

        return dto;
    }
}
