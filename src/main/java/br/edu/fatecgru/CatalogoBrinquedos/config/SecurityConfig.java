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
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. O que TODO MUNDO pode fazer (Público)
                // Incluímos a visualização E a encomenda aqui
                .requestMatchers(HttpMethod.GET, "/api/brinquedos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/brinquedos/encomendar/**").permitAll() 
                
                // 2. O que só o ADMIN pode fazer (Protegido por Senha)
                // Basicamente tudo que altera o catálogo ou o estoque administrativo
                .requestMatchers(HttpMethod.POST, "/api/brinquedos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/brinquedos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/brinquedos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/brinquedos/**").hasRole("ADMIN")
                
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
                        .allowedOrigins("*") // No futuro, troque pelo endereço do site React
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
            }
        };
    }
}