package br.edu.fatecgru.CatalogoBrinquedos.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User; // Importante para o builder
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Usuario;
import br.edu.fatecgru.CatalogoBrinquedos.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Busca o usuário no seu banco pelo login
        Usuario usuario = repository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));

        // Converte o seu Usuario do banco para o UserDetails que o Spring entende
        return org.springframework.security.core.userdetails.User.builder()
            .username(usuario.getLogin())
            .password(usuario.getSenha())
            .roles(usuario.getPerfil().name().replace("ROLE_", "")) // Converte ROLE_ADMIN para ADMIN
            .build();
    }
}