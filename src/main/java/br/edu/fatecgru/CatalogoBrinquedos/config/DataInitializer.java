package br.edu.fatecgru.CatalogoBrinquedos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Perfil;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Usuario;
import br.edu.fatecgru.CatalogoBrinquedos.repository.UsuarioRepository;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o banco está vazio de usuários
        if (usuarioRepository.count() == 0) {
            
            // Criando o Administrador (Acesso Total)
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("admin123")); // Criptografando a senha!
            admin.setPerfil(Perfil.ROLE_ADMIN);
            usuarioRepository.save(admin);

            // Criando um Usuário Comum (Acesso Limitado)
            Usuario user = new Usuario();
            user.setLogin("joao");
            user.setSenha(passwordEncoder.encode("user123"));
            user.setPerfil(Perfil.ROLE_USER);
            usuarioRepository.save(user);

            System.out.println(">>> Usuários padrão criados: admin/admin123 e joao/user123");
        }
    }
}