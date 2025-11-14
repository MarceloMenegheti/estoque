package com.menegheti.teste_estoque.model.DTO;

import java.util.Objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class FornecedorDTO {
	
	
    private Long id;
    
    @NotNull(message = "Nome é obrigatório")  
    @NotBlank(message = "Nome não pode ser vazio")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    private String telefone;
    
    // Atualizado: Validação de formato (só se fornecido; opcional)
    @Size(max = 120, message = "Email deve ter no máximo 120 caracteres")
    @Email(message = "Formato de email inválido (ex.: user@domain.com)")  // Novo: Valida formato
    private String email;
    
    @NotNull(message = "CNPJ é obrigatório")
    @NotBlank(message = "CNPJ não pode ser vazio")
    @Size(max = 20, message = "CNPJ deve ter no máximo 20 caracteres")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "Formato de CNPJ inválido (ex.: 12.345.678/0001-99)")
    private String cnpj;
    
    // ← NOVO: Nome do fornecedor para exibição na listagem (não usado no cadastro)
    private String fornecedorNome;

    public FornecedorDTO() {}

    public FornecedorDTO(Long id, String nome, String telefone, String email, String cnpj) {
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
    
    public String getTelefone() {return telefone;}
    
    public void setTelefone(String telefone) { this.telefone = telefone;}
    
    public String getEmail() {return email;}
    
    public void setEmail(String email) {this.email = email;}
    
    public String getCnpj() {return cnpj;}

	public void setCnpj(String cnpj) {this.cnpj = cnpj;}
	
    // ← NOVO: Getter e Setter para fornecedorNome
    public String getFornecedorNome() { return fornecedorNome; }
    public void setFornecedorNome(String fornecedorNome) { this.fornecedorNome = fornecedorNome; }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FornecedorDTO other = (FornecedorDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "FornecedorDTO [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email  + cnpj + "]" ;
	}
    
    
    
}
