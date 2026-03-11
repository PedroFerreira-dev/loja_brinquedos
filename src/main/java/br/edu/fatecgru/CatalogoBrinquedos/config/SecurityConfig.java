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
            // ATIVAÇÃO DO CORS: Essencial para que o Spring Security aceite as 
            // configurações de permissão vindas do React (localhost:5173)
            .cors(Customizer.withDefaults()) 
            
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. O que TODO MUNDO pode fazer (Público)
                // Incluímos a visualização E a encomenda aqui
                .requestMatchers(HttpMethod.GET, "/api/brinquedos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/brinquedos/encomendar/**").permitAll() 
                
                // 2. O que só o ADMIN pode fazer (Protegido por Senha)
                // Basicamente tudo que altera o catálogo ou o estoque administrativo
                // Alteramos de .hasRole("ADMIN") para .authenticated() para facilitar a 
                // integração inicial, garantindo que qualquer login válido acesse o Admin.
                .requestMatchers(HttpMethod.POST, "/api/brinquedos").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/brinquedos/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/brinquedos/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/api/brinquedos/**").authenticated()
                
                // Qualquer outra rota não mapeada exige login
                .anyRequest().authenticated() 
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
                        // IMPORTANTE: origins("*") não funciona com allowCredentials(true)
                        // Por isso, definimos o endereço exato do seu Front-end
                        .allowedOrigins("http://localhost:5173") 
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowCredentials(true); // Permite o envio de cookies/auth headers
            }
        };
    }
}