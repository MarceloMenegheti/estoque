package com.menegheti.teste_estoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.menegheti.teste_estoque.model.DTO.FornecedorDTO;
import com.menegheti.teste_estoque.model.DTO.MovimentacaoEstoqueDTO;
import com.menegheti.teste_estoque.model.DTO.ProdutoDTO;
import com.menegheti.teste_estoque.model.DTO.UsuarioDTO;
import com.menegheti.teste_estoque.model.enums.Perfil;
import com.menegheti.teste_estoque.services.FornecedorService;
import com.menegheti.teste_estoque.services.MovimentacaoEstoqueService;
import com.menegheti.teste_estoque.services.ProdutoService;
import com.menegheti.teste_estoque.services.UsuarioService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FornecedorService fornecedorService; 

    @Autowired
    private ProdutoService produtoService; 

    @Autowired
    private MovimentacaoEstoqueService movimentacaoService; 


    @Override
    public void run(String... args) throws Exception {
        inicializarUsuarios();
        inicializarFornecedores();
        inicializarProdutos();
        inicializarMovimentacoes();
        System.out.println("Inicialização de dados concluída!");
    }

    private void inicializarUsuarios() {
        UsuarioDTO adminDto = new UsuarioDTO(null, "Admin Default", "admin@exemplo.com", "admin123", Perfil.ADMIN);
        // Verifica se já existe pelo email
        if (!usuarioService.listar().stream().anyMatch(u -> u.getEmail().equals(adminDto.getEmail()))) {
            usuarioService.salvar(adminDto);
            System.out.println("Usuário admin criado no startup!");
        }
        
        UsuarioDTO userDto = new UsuarioDTO(null, "Usuário Teste", "user@exemplo.com", "user123", Perfil.ESTOQUISTA);
        if (!usuarioService.listar().stream().anyMatch(u -> u.getEmail().equals(userDto.getEmail()))) {
            usuarioService.salvar(userDto);
            System.out.println("Usuário teste criado no startup!");
        }
    }

    private void inicializarFornecedores() {
        // Exemplo de fornecedor
        FornecedorDTO fornecedorDto = new FornecedorDTO();
        fornecedorDto.setNome("ABC Ltda");
        fornecedorDto.setTelefone("(11) 99999-9999");
        fornecedorDto.setEmail("contato@abc.com.br");
        fornecedorDto.setCnpj("12.345.678/0001-99"); // Formato válido conforme @Pattern

        // Verifica se já existe pelo CNPJ (em vez de nome, para evitar duplicatas de CNPJ)
        if (!fornecedorService.listar().stream().anyMatch(f -> f.getCnpj().equals(fornecedorDto.getCnpj()))) {
            fornecedorService.criar(fornecedorDto); // Assumindo método criar no service
            System.out.println("Fornecedor ABC criado no startup!");
        }

        // Exemplo adicional: Outro fornecedor
        FornecedorDTO fornecedor2Dto = new FornecedorDTO();
        fornecedor2Dto.setNome("XYZ Importados");
        fornecedor2Dto.setTelefone("(21) 88888-8888");
        fornecedor2Dto.setEmail("vendas@xyz.com");
        fornecedor2Dto.setCnpj("98.765.432/1000-12");

        if (!fornecedorService.listar().stream().anyMatch(f -> f.getCnpj().equals(fornecedor2Dto.getCnpj()))) {
            fornecedorService.criar(fornecedor2Dto);
            System.out.println("Fornecedor XYZ criado no startup!");
        }
    }

    private void inicializarProdutos() {
        // Assumindo que o primeiro fornecedor foi criado com ID 1 (ajuste se necessário)
        Long fornecedorId = 1L; // Busque dinamicamente se possível: fornecedorService.buscarPorId(1L).getId()

        // Exemplo de produto
        ProdutoDTO produtoDto = new ProdutoDTO();
        produtoDto.setNome("Produto Teste 1");
        produtoDto.setDescricao("Descrição do produto de teste");
        produtoDto.setQuantidade(10);
        produtoDto.setPreco(new BigDecimal("29.99"));
        produtoDto.setFornecedorId(fornecedorId);
        produtoDto.setCategoria("Eletronico");


        // Verifica se já existe pelo nome
        if (!produtoService.listar().stream().anyMatch(p -> p.getNome().equals(produtoDto.getNome()))) {
            produtoService.salvar(produtoDto); // Assumindo método criar no service
            System.out.println("Produto Teste 1 criado no startup!");
        }

        // Exemplo adicional: Outro produto
        ProdutoDTO produto2Dto = new ProdutoDTO();
        produto2Dto.setNome("Produto Teste 2");
        produto2Dto.setDescricao("Outro produto de teste");
        produto2Dto.setQuantidade(5);
        produto2Dto.setPreco(new BigDecimal("49.99"));
        produto2Dto.setFornecedorId(fornecedorId);
        produto2Dto.setCategoria("OUTROS");

        if (!produtoService.listar().stream().anyMatch(p -> p.getNome().equals(produto2Dto.getNome()))) {
            produtoService.salvar(produto2Dto);
            System.out.println("Produto Teste 2 criado no startup!");
        }
    }

    private void inicializarMovimentacoes() {
        // Assumindo IDs: usuário 1 (admin), produto 1 (criado acima)
        Long usuarioId = 1L;
        Long produtoId = 1L;

        // Exemplo de movimentação de entrada
        MovimentacaoEstoqueDTO movDto = new MovimentacaoEstoqueDTO();
        movDto.setProdutoId(produtoId);
        movDto.setUsuarioId(usuarioId);
        movDto.setQuantidade(20);
        movDto.setTipo("ENTRADA"); // Assumindo string; ajuste para enum se necessário
        movDto.setData(LocalDateTime.now());

        // Verifica se já existe (simples, por tipo e data aproximada)
        if (!movimentacaoService.listar().stream()
                .anyMatch(m -> m.getTipo().equals(movDto.getTipo()) && m.getData().isAfter(LocalDateTime.now().minusDays(1)))) {
            movimentacaoService.salvar(movDto); // Assumindo método criar no service
            System.out.println("Movimentação de entrada criada no startup!");
        }

        // Exemplo adicional: Movimentação de saída
        MovimentacaoEstoqueDTO mov2Dto = new MovimentacaoEstoqueDTO();
        mov2Dto.setProdutoId(produtoId);
        mov2Dto.setUsuarioId(usuarioId);
        mov2Dto.setQuantidade(5);
        mov2Dto.setTipo("SAIDA");
        mov2Dto.setData(LocalDateTime.now().minusHours(1));

        if (!movimentacaoService.listar().stream()
                .anyMatch(m -> m.getTipo().equals(mov2Dto.getTipo()) && m.getData().isAfter(LocalDateTime.now().minusDays(1)))) {
            movimentacaoService.salvar(mov2Dto);
            System.out.println("Movimentação de saída criada no startup!");
        }
    }
}