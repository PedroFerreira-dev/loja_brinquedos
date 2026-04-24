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
import br.edu.fatecgru.CatalogoBrinquedos.repository.EncomendaRepository;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BrinquedoRepository brinquedoRepository;

    @Autowired
    private EncomendaRepository encomendaRepository;

    @Autowired
    private br.edu.fatecgru.CatalogoBrinquedos.repository.CategoriaRepository categoriaRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // Força o banco de dados a aceitar imagens longas (Base64)
        try {
            jdbcTemplate.execute("ALTER TABLE brinquedo MODIFY caminho_imagem LONGTEXT");
            
            // Limpeza Definitiva: Remove o Cachorro Maluco (ID 367) e a categoria Animais
            jdbcTemplate.execute("DELETE FROM encomenda WHERE brinquedo_id = 367 OR brinquedo_id IN (SELECT id FROM brinquedo WHERE UPPER(categoria) LIKE '%ANIMAIS%')");
            jdbcTemplate.execute("DELETE FROM brinquedo WHERE id = 367 OR UPPER(categoria) LIKE '%ANIMAIS%'");
            jdbcTemplate.execute("DELETE FROM categoria WHERE UPPER(nome) LIKE '%ANIMAIS%'");
        } catch (Exception e) {
            System.out.println("Nota: Itens já removidos ou tabelas ainda não criadas.");
        }
        
        // Inicialização das Categorias Permanentes
        if (categoriaRepository.count() == 0) {
            List<String> iniciais = Arrays.asList("Ação", "Bonecas", "Educativos", "Heróis", "Pelúcias", "Tabuleiro", "Veículos");
            iniciais.forEach(nome -> categoriaRepository.save(new br.edu.fatecgru.CatalogoBrinquedos.model.entity.Categoria(nome)));
        }

        // Inicialização dos usuários padrão do sistema
        if (usuarioRepository.count() == 0) {
            // Criação do perfil Administrador
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setNome("Administrador ToyUniverse");
            admin.setCpf("000.000.000-00");
            admin.setEmail("admin@gmail.com"); 
            admin.setPerfil(Perfil.ROLE_ADMIN);
            admin.setAtivo(true);
            usuarioRepository.save(admin);

            // Criação de um usuário comum para testes de fluxo de compra
            Usuario user = new Usuario();
            user.setLogin("joao@email.com");
            user.setSenha(passwordEncoder.encode("user123"));
            user.setNome("João Silva");
            user.setCpf("111.111.111-11");
            user.setEmail("joao@email.com");
            user.setPerfil(Perfil.ROLE_USER);
            user.setAtivo(true);
            usuarioRepository.save(user);
        }

        // Cadastro do catálogo de brinquedos inicial - APENAS SE ESTIVER VAZIO
        if (brinquedoRepository.count() == 0) {
            Brinquedo b1 = criarBrinquedo("Carrinho Lego Batman Off-Road", "Veículos", 149.90, 15.0, 15, "Carrinho de controle remoto.", "https://images.unsplash.com/photo-1594736797933-d0501ba2fe65?w=500");
            Brinquedo b2 = criarBrinquedo("Guincho Tubarão Hot-Wheels", "Veículos", 299.00, 0.0, 8, "Guincho Tubarão Hot-Wheels.", "https://www.anneclairebaby.com/cdn/shop/products/Wholesale-of-Hot-Wheels-City-Shark-Chomp-Transporter-HWS-TOY21-2.jpg?v=1669384379&width=14450");
            Brinquedo b3 = criarBrinquedo("Helicóptero de Combate", "Veículos", 350.00, 10.0, 5, "Helicóptero Militar.", "https://ae01.alicdn.com/kf/S9369471795344bfcbed497e203843ada4.jpg");
            Brinquedo b4 = criarBrinquedo("Jogo de Tabuleiro Detetive", "Tabuleiro", 89.90, 0.0, 20, "Detetive.", "https://a-static.mlcdn.com.br/800x600/jogo-detetive-estrela/magazineluiza/181245800/4e2b729939b08b81746e126827d4a537.jpg");
            Brinquedo b5 = criarBrinquedo("Banco Imobiliário", "Tabuleiro", 120.00, 5.0, 12, "Jogo de estratégia.", "https://images.tcdn.com.br/img/img_prod/721441/jogo_banco_imobiliario_realidade_aumentada_estrela_9773_1_1b71e436f7223c4e1bf6f6b7b191ccfd.png");
            Brinquedo b6 = criarBrinquedo("Jogo de Damas", "Tabuleiro", 45.00, 0.0, 30, "Peças em acrílico.", "https://m.media-amazon.com/images/I/71DxExWBmsL.jpg");
            Brinquedo b7 = criarBrinquedo("Urso Gigante Pelúcia", "Pelúcias", 199.90, 20.0, 10, "1 metro de fofura.", "https://images.unsplash.com/photo-1559454403-b8fb88521f11?w=500");
            Brinquedo b10 = criarBrinquedo("Boneco Spider-Man", "Heróis", 180.00, 10.0, 15, "Articulado.", "https://a-static.mlcdn.com.br/420x420/brinquedo-homem-aranha-traje-dourado-original-ideal-para-presente-super-heroi-medio-com-garantia-blackwatch/blackwatchltda/homem-aranha-dourado-111/28f3e543785e9364520131503e9b8371.jpeg");
            Brinquedo b11 = criarBrinquedo("Batman o Cavaleiro das Trevas", "Heróis", 195.00, 0.0, 10, "Cavaleiro das Trevas o Retorno.", "https://images.tcdn.com.br/img/img_prod/460977/action_figure_batman_mafex_batman_the_dark_knight_triumphant_no119_medicom_mkp_114995_1_a96c4d7eadebe67f2d56f3290effe030.jpeg");
            Brinquedo b12 = criarBrinquedo("Homem de Ferro", "Heróis", 450.00, 25.0, 3, "Colecionador.", "https://bumerangbrinquedos.vteximg.com.br/arquivos/ids/275395/Boneco-Homem-de-Ferro.png?v=638371898403830000");
            Brinquedo b13 = criarBrinquedo("Dinossauro T-Rex", "Ação", 130.00, 0.0, 18, "Movimentos reais.", "https://rihappy.vtexassets.com/arquivos/ids/8327324-800-auto?v=638842113540730000&width=800&height=auto&aspect=true");
            Brinquedo b14 = criarBrinquedo("Super Lançador de Água", "Ação", 65.00, 5.0, 40, "Verão divertido.", "https://m.media-amazon.com/images/I/61upHpEZ-oL._AC_UF894,1000_QL80_.jpg");
            Brinquedo b15 = criarBrinquedo("Espada Samurai", "Ação", 55.00, 0.0, 22, "Seguro para crianças.", "https://http2.mlstatic.com/D_NQ_NP_978652-MLA97215854165_112025-O.webp");
            Brinquedo b16 = criarBrinquedo("Blocos de Montar Casinha", "Educativos", 250.00, 15.0, 20, "500 peças.", "https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcQ_fu6hG24kiXpTYqUheoKR4GzuWTF3ZyC5IfWEa7i4uMHDXgSMCH4nEYWkCQbUSfHbgzGIbuW-PUIdZg_NnJJgui2xH3lLauPZ5wbyrw2VyGBqD5g1FsraFx9qx5wZS94auoAy9w&usqp=CAc");
            Brinquedo b17 = criarBrinquedo("Quebra-Cabeça Mapa Dora", "Educativos", 49.90, 0.0, 35, "1000 peças.", "https://m.media-amazon.com/images/I/81eHOfaET1S._AC_UF1000,1000_QL80_.jpg");
            Brinquedo b18 = criarBrinquedo("Abecedário Madeira", "Educativos", 75.00, 0.0, 25, "Alfabetização.", "https://images.unsplash.com/photo-1515488042361-ee00e0ddd4e4?w=500");
            Brinquedo b20 = criarBrinquedo("Casinha de Boneca Sonho", "Bonecas", 550.00, 12.0, 4, "MDF mobiliado.", "https://m.media-amazon.com/images/I/61Q7bXtRQqL._AC_UF894,1000_QL80_.jpg");

            brinquedoRepository.saveAll(Arrays.asList(b1, b2, b3, b4, b5, b6, b7, b10, b11, b12, b13, b14, b15, b16, b17, b18, b20));
        }
    }

    // Método auxiliar para simplificar a criação de instâncias de Brinquedo
    private Brinquedo criarBrinquedo(String nome, String categoria, Double preco, Double desconto, Integer quantidade, String descricao, String imagem) {
        Brinquedo b = new Brinquedo();
        b.setNome(nome); 
        b.setCategoria(categoria); 
        b.setPreco(preco);
        b.setDesconto(desconto); 
        b.setQuantidade(quantidade); 
        b.setDescricao(descricao);
        b.setCaminhoImagem(imagem); 
        b.setVendas(0);
        return b;
    }
}