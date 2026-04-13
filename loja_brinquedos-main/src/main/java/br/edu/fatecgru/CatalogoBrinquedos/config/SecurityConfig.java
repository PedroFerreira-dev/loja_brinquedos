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
                // Permite ver produtos, encomendar e CRIAR CONTA sem login
                .requestMatchers(HttpMethod.GET, "/api/brinquedos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/brinquedos/encomendar/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() 
                
                // Apenas ADMIN pode gerenciar o estoque
                .requestMatchers(HttpMethod.POST, "/api/brinquedos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/brinquedos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/brinquedos/**").hasRole("ADMIN")
                
                .anyRequest().authenticated() 
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}