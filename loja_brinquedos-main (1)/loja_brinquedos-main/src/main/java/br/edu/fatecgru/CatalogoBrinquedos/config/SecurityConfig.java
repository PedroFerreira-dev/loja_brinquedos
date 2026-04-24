package br.edu.fatecgru.CatalogoBrinquedos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. ACESSO PÚBLICO (Site, Estilos e Consulta)
                .requestMatchers("/", "/index.html", "/editar.html", "/static/**", "/css/**", "/js/**", "/favicon.ico").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/brinquedos/**").permitAll() // Ver catálogo, destaques, etc.
                .requestMatchers(HttpMethod.POST, "/api/usuarios/registrar").permitAll() // Criar conta
                
                // 2. EXIGE LOGIN (Usuário comum ou Admin)
                .requestMatchers(HttpMethod.POST, "/api/brinquedos/encomendar/**").authenticated() 
                .requestMatchers(HttpMethod.GET, "/api/brinquedos/historico").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/brinquedos/encomendar/*/confirmar").authenticated()
                .requestMatchers("/api/usuarios/me", "/api/usuarios/login").authenticated()
                
                // 3. APENAS ADMINISTRADOR (Gerenciar Catálogo)
                .requestMatchers(HttpMethod.GET, "/api/brinquedos/encomendas/todas").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/brinquedos/encomendar/*/status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/brinquedos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/brinquedos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/brinquedos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/brinquedos/**").hasRole("ADMIN")
                
                // Garantir que qualquer outra página HTML ou recurso não mapeado também seja público
                .anyRequest().permitAll() 
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Nosso embaralhador de senhas
    }

    // Libera o acesso para o React (CORS)
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // No futuro, troque pelo endereço do site React
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization");
            }
        };
    }
}