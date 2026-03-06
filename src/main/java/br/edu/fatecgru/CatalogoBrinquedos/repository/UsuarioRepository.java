package br.edu.fatecgru.CatalogoBrinquedos.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // O Spring Security vai usar isso para validar se o login existe
    Optional<Usuario> findByLogin(String login);
}