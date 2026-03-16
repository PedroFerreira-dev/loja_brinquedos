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
	            // 1. Público
	            .requestMatchers(HttpMethod.GET, "/api/brinquedos/**").permitAll()
	            .requestMatchers(HttpMethod.POST, "/api/brinquedos/encomendar/**").permitAll()
	            .requestMatchers("/imagens/**").permitAll() // NOVO: Permite ver as fotos salvas
	            
	            // 2. Protegido
	            .requestMatchers(HttpMethod.POST, "/api/brinquedos/upload").authenticated() // NOVO: Protege o endpoint de upload
	            .requestMatchers(HttpMethod.POST, "/api/brinquedos").authenticated()
	            .requestMatchers(HttpMethod.PUT, "/api/brinquedos/**").authenticated()
	            .requestMatchers(HttpMethod.DELETE, "/api/brinquedos/**").authenticated()
	            .requestMatchers(HttpMethod.PATCH, "/api/brinquedos/**").authenticated()
	            
	            .anyRequest().authenticated() 
	        )
	        // ALTERADO: Impede o pop-up de login do navegador (Basic Auth Challenge)
	        .httpBasic(basic -> basic.authenticationEntryPoint((request, response, authException) -> {
	            // Em vez de retornar o cabeçalho WWW-Authenticate (que abre o pop-up), 
	            // retornamos apenas o erro 401 puro para o Axios tratar no React.
	            response.sendError(402, authException.getMessage()); 
	        }));

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