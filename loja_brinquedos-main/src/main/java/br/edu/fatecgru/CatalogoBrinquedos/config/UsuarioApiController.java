package br.edu.fatecgru.CatalogoBrinquedos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Perfil;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Usuario;
import br.edu.fatecgru.CatalogoBrinquedos.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioApiController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> criarConta(@RequestBody Usuario usuario) {
        // Todo novo usuário criado pelo site será ROLE_USER por segurança
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setPerfil(Perfil.ROLE_USER);
        repository.save(usuario);
        return ResponseEntity.ok("Conta criada com sucesso! Agora você pode fazer pedidos.");
    }
}