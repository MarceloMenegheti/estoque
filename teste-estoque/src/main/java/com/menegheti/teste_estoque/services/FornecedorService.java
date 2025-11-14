package com.menegheti.teste_estoque.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.menegheti.teste_estoque.model.DTO.FornecedorDTO;
import com.menegheti.teste_estoque.model.entities.Fornecedor;
import com.menegheti.teste_estoque.repositories.FornecedorRepository;

@Service
public class FornecedorService {

    private final FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Fornecedor salvar(Fornecedor fornecedor) {
        // Validações de null (existentes)
        if (fornecedor.getCnpj() == null || fornecedor.getCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ é obrigatório e não pode ser nulo");
        }
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório e não pode ser nulo");
        }
        
        Optional<Fornecedor> fornecedorComCnpj = repository.findByCnpj(fornecedor.getCnpj());
        if (fornecedorComCnpj.isPresent() && !fornecedorComCnpj.get().getId().equals(fornecedor.getId())) {
            throw new IllegalArgumentException("CNPJ já cadastrado para outro fornecedor");
        }
        
        return repository.save(fornecedor);
    }
    
    @Transactional
    public FornecedorDTO criar(FornecedorDTO dto) { 

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }

        // Converte DTO → Entidade
        Fornecedor entidade = fromDTO(dto);     
        Fornecedor salvo = salvar(entidade);
        
        return toDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<FornecedorDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<FornecedorDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    @Transactional
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public FornecedorDTO toDTO(Fornecedor f) {
        return new FornecedorDTO(
                f.getId(),
                f.getNome(),
                f.getTelefone(),
                f.getEmail(),
                f.getCnpj()
        );
    }
    
    @Transactional(readOnly = true)
    public Optional<Fornecedor> buscarEntidadePorId(Long id) {
        return repository.findById(id);
    }
        
    public Fornecedor fromDTO(FornecedorDTO dto) {
        Fornecedor entidade = new Fornecedor();
        entidade.setId(dto.getId());
        entidade.setNome(dto.getNome());
        entidade.setCnpj(dto.getCnpj());
        entidade.setEmail(dto.getEmail());
        entidade.setTelefone(dto.getTelefone());
        return entidade;
    }
    
    public void atualizarFromDTO(Fornecedor entidade, FornecedorDTO dto) {
        entidade.setNome(dto.getNome());
        entidade.setCnpj(dto.getCnpj());
        
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            entidade.setEmail(dto.getEmail());
        }
        if (dto.getTelefone() != null && !dto.getTelefone().trim().isEmpty()) {
            entidade.setTelefone(dto.getTelefone());
        }
    }

}

