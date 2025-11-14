package com.menegheti.teste_estoque.services;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.menegheti.teste_estoque.model.DTO.UsuarioDTO;
import com.menegheti.teste_estoque.model.entities.Usuario;
import com.menegheti.teste_estoque.model.enums.Perfil;
import com.menegheti.teste_estoque.model.exceptions.EmailJaCadastradoException;
import com.menegheti.teste_estoque.model.exceptions.UsuarioNaoEncontradoException;
import com.menegheti.teste_estoque.repositories.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {
	
    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UsuarioDTO salvar(UsuarioDTO dto) {
        // Se ID presente, é update; senão, criação
        boolean isUpdate = dto.getId() != null;

        // Validação email único (para criação ou update)
        if (!isUpdate && repository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado: " + dto.getEmail());
        }
        if (isUpdate && repository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
            throw new EmailJaCadastradoException("Email já cadastrado por outro usuário: " + dto.getEmail());
        }

        Usuario usuario;
        if (isUpdate) {
            usuario = repository.findById(dto.getId())
                    .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + dto.getId()));
        } else {
            usuario = new Usuario();
        }

        // Atualiza campos
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setPerfil(dto.getPerfil() != null ? dto.getPerfil() : Perfil.ESTOQUISTA);

        // Hash senha: Sempre para criação; para update, só se fornecida
        if (!isUpdate || (dto.getSenha() != null && !dto.getSenha().trim().isEmpty())) {
            if (dto.getSenha() == null || dto.getSenha().length() < 6) {
                throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
            }
            usuario.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        }

        Usuario salvo = repository.save(usuario);
        return toDTO(salvo);
    }

    
    public UsuarioDTO atualizar(UsuarioDTO dto, Long id) {
        // Encontra usuário
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + id));
        // Validação email único (exceto para si mesmo)
        if (repository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new EmailJaCadastradoException("Email já cadastrado por outro usuário: " + dto.getEmail());
        }
        // Atualiza campos básicos
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setPerfil(dto.getPerfil() != null ? dto.getPerfil() : usuario.getPerfil());
        if (dto.getSenha() != null && !dto.getSenha().trim().isEmpty()) {
            if (dto.getSenha().length() < 6) {
                throw new IllegalArgumentException("Nova senha deve ter pelo menos 6 caracteres");
            }
            String novaSenhaHash = passwordEncoder.encode(dto.getSenha());
            usuario.setSenhaHash(novaSenhaHash); 
        }
        Usuario atualizado = repository.save(usuario);
        return toDTO(atualizado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome) // método do repository
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        repository.deleteById(id);
    }
    
    @Transactional
    public void promoverParaAdmin(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        usuario.setPerfil(Perfil.ADMIN);
        repository.save(usuario);
    }
    
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }

    // Mapeia entity → DTO (sem senha)
    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setPerfil(usuario.getPerfil());
        // NÃO setSenha (fica null para output)
        return dto;
    }

    @Transactional(readOnly = true)
    public long contarTotalUsuarios() {
        return repository.count();
    }

}


