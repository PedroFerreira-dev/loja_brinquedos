package br.edu.fatecgru.CatalogoBrinquedos;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Isso cria uma "ponte" entre a URL /imagens/ e a sua pasta física uploads/
        registry.addResourceHandler("/imagens/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
    }
}