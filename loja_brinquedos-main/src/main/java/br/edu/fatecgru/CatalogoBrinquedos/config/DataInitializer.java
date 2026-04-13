package br.edu.fatecgru.CatalogoBrinquedos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Brinquedo;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Perfil;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Usuario;
import br.edu.fatecgru.CatalogoBrinquedos.repository.BrinquedoRepository;
import br.edu.fatecgru.CatalogoBrinquedos.repository.UsuarioRepository;

import java.util.Arrays;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BrinquedoRepository brinquedoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Inicialização de Usuários
        if (usuarioRepository.count() == 0) {
            
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setPerfil(Perfil.ROLE_ADMIN);
            usuarioRepository.save(admin);

            Usuario user = new Usuario();
            user.setLogin("joao");
            user.setSenha(passwordEncoder.encode("user123"));
            user.setPerfil(Perfil.ROLE_USER);
            usuarioRepository.save(user);

            System.out.println(">>> Usuários padrão criados: admin/admin123 e joao/user123");
        }

        // 2. Inicialização de Brinquedos (Catálogo Padrão)
        if (brinquedoRepository.count() == 0) {

            Brinquedo b1 = new Brinquedo();
            b1.setNome("Carrinho Off-Road 4x4");
            b1.setCategoria("Veículos");
            b1.setPreco(149.90);
            b1.setQuantidade(15);
            b1.setDescricao("Carrinho de controle remoto de alta velocidade, ideal para terrenos irregulares.");
            b1.setCaminhoImagem("https://images.unsplash.com/photo-1594736797933-d0501ba2fe65?w=500&auto=format&fit=crop&q=60");

            Brinquedo b2 = new Brinquedo();
            b2.setNome("Jogo de Xadrez Clássico");
            b2.setCategoria("Tabuleiro");
            b2.setPreco(89.90);
            b2.setQuantidade(20);
            b2.setDescricao("Tabuleiro em madeira com peças detalhadas, perfeito para desenvolver o raciocínio.");
            b2.setCaminhoImagem("https://images.unsplash.com/photo-1586165368502-1bad197a6461?w=500&auto=format&fit=crop&q=60");

            Brinquedo b3 = new Brinquedo();
            b3.setNome("Kit Blocos de Construção");
            b3.setCategoria("Educativos");
            b3.setPreco(210.00);
            b3.setQuantidade(30);
            b3.setDescricao("500 peças coloridas para montar cidades, carros e o que a imaginação permitir.");
            b3.setCaminhoImagem("https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=500&auto=format&fit=crop&q=60");

            Brinquedo b4 = new Brinquedo();
            b4.setNome("Urso de Pelúcia Gigante");
            b4.setCategoria("Pelúcias");
            b4.setPreco(199.90);
            b4.setQuantidade(5);
            b4.setDescricao("Super macio e fofinho, com 1 metro de altura.");
            b4.setCaminhoImagem("https://images.unsplash.com/photo-1559454403-b8fb88521f11?w=500&auto=format&fit=crop&q=60");

            Brinquedo b5 = new Brinquedo();
            b5.setNome("Action Figure Super Herói");
            b5.setCategoria("Ação");
            b5.setPreco(95.00);
            b5.setQuantidade(25);
            b5.setDescricao("Boneco articulado com luzes e sons de batalha.");
            b5.setCaminhoImagem("https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?w=500&auto=format&fit=crop&q=60");

            Brinquedo b6 = new Brinquedo();
            b6.setNome("Boneca Aventureira");
            b6.setCategoria("Bonecas");
            b6.setPreco(119.50);
            b6.setQuantidade(12);
            b6.setDescricao("Acompanha mochila, binóculos e acessórios de acampamento.");
            b6.setCaminhoImagem("https://images.unsplash.com/photo-1558066130-1c3909e46a78?w=500&auto=format&fit=crop&q=60");

            // Salva todos os brinquedos no banco de dados
            brinquedoRepository.saveAll(Arrays.asList(b1, b2, b3, b4, b5, b6));

            System.out.println(">>> Catálogo de brinquedos inicializado com 6 produtos.");
        }
    }
}