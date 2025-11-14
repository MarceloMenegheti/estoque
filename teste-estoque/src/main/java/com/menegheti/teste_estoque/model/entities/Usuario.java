package com.menegheti.teste_estoque.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.menegheti.teste_estoque.model.enums.Perfil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios") 
public class Usuario implements Serializable, UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false, length = 20)
    private Perfil perfil;
    
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

    
    public Usuario() {}
    
    public Usuario(Long id, String nome, String email, String senhaHash, Perfil perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
    }

	public Long getId() {return id;}

	public void setId(Long id) {this.id = id;}

	public String getNome() {return nome;}

	public void setNome(String nome) {this.nome = nome;}

	public String getEmail() {return email;}

	public void setEmail(String email) {this.email = email;}

	public String getSenhaHash() {return senhaHash;}

	public void setSenhaHash(String senhaHash) {this.senhaHash = senhaHash;}

	public Perfil getPerfil() {return perfil;}

	public void setPerfil(Perfil perfil) {this.perfil = perfil;}
 
    public List<MovimentacaoEstoque> getMovimentacoes() { return movimentacoes; }
    public void setMovimentacoes(List<MovimentacaoEstoque> movimentacoes) { this.movimentacoes = movimentacoes; }
	

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
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte perfil em authority: "ROLE_ADMIN" ou "ROLE_ESTOQUISTA"
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }
    
    @Override
    public String getPassword() {return senhaHash;}
    @Override
    public String getUsername() {return email;}
    
    @Override
    public boolean isAccountNonExpired() { return true; }  
    @Override
    public boolean isAccountNonLocked() { return true; } 
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
