package br.edu.fatecgru.CatalogoBrinquedos.service;

import br.edu.fatecgru.CatalogoBrinquedos.dto.UsuarioRequestDTO;
import br.edu.fatecgru.CatalogoBrinquedos.dto.UsuarioResponseDTO;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Perfil;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Usuario;
import br.edu.fatecgru.CatalogoBrinquedos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) {
        if (repository.findByLogin(request.getLogin()).isPresent()) {
            throw new RuntimeException("Login já existe");
        }
        if (repository.findByCpf(request.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(request.getLogin());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setNome(request.getNome());
        usuario.setCpf(request.getCpf());
        usuario.setEmail(request.getEmail());
        usuario.setEndereco(request.getEndereco());
        usuario.setPerfil(request.getPerfil() != null ? request.getPerfil() : Perfil.ROLE_USER);

        Usuario salvo = repository.save(usuario);
        return converterParaResponse(salvo);
    }

    public Usuario buscarEntidadePorLogin(String login) {
        return repository.findByLogin(login)
                .filter(Usuario::isAtivo)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou inativo"));
    }

    public UsuarioResponseDTO buscarPorLogin(String login) {
        return converterParaResponse(buscarEntidadePorLogin(login));
    }

    public void desativarConta(String login) {
        Usuario usuario = buscarEntidadePorLogin(login);
        
        // Trava de segurança: Administradores não podem excluir a própria conta
        if (usuario.getPerfil() == Perfil.ROLE_ADMIN) {
            throw new RuntimeException("Administradores não podem desativar sua própria conta por segurança.");
        }

        usuario.setAtivo(false);
        repository.save(usuario);
    }

    private UsuarioResponseDTO converterParaResponse(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getEndereco(),
                usuario.getPerfil()
        );
    }
}
