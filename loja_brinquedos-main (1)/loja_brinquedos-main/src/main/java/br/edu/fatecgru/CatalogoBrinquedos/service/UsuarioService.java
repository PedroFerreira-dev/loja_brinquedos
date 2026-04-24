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

    // Método responsável pelo cadastro de novos usuários no sistema
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) {
        
        // Verifica se o login ou CPF já estão presentes na base de dados
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
        
        // Atribui estritamente o perfil de usuário comum (ROLE_USER) para evitar elevação de privilégios
        usuario.setPerfil(Perfil.ROLE_USER);
        
        // Define o status da conta como ativa por padrão
        usuario.setAtivo(true);

        Usuario salvo = repository.save(usuario);
        return converterParaResponse(salvo);
    }

    // Busca a entidade Usuario pelo login, garantindo que a conta esteja com status ativo
    public Usuario buscarEntidadePorLogin(String login) {
        return repository.findByLogin(login)
                .filter(Usuario::isAtivo)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou inativo"));
    }

    // Retorna os dados do usuário formatados no padrão de transferência (DTO)
    public UsuarioResponseDTO buscarPorLogin(String login) {
        return converterParaResponse(buscarEntidadePorLogin(login));
    }

    // Método responsável por realizar a desativação lógica da conta do usuário
    public void desativarConta(String login) {
        Usuario usuario = buscarEntidadePorLogin(login);
        
        // Restrição de regra de negócio: Administradores não têm permissão para desativar a própria conta
        if (usuario.getPerfil() == Perfil.ROLE_ADMIN) {
            throw new RuntimeException("Administradores não podem desativar sua própria conta.");
        }

        usuario.setAtivo(false);
        repository.save(usuario);
    }

    // Método utilitário para converter a entidade Usuario em UsuarioResponseDTO
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