package br.edu.fatecgru.CatalogoBrinquedos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String mostrarPaginaInicial() {
        // aqui diz pro Spring para ir na pasta templates e mostrar oarquivo index.html
        return "index"; 
    }
}