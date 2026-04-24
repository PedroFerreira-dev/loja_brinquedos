package br.edu.fatecgru.CatalogoBrinquedos.controller;

import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Categoria;
import br.edu.fatecgru.CatalogoBrinquedos.repository.CategoriaRepository;
import br.edu.fatecgru.CatalogoBrinquedos.service.BrinquedoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaApiController {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private BrinquedoService brinquedoService;

    @GetMapping
    public List<String> listar() {
        return repository.findAll().stream().map(Categoria::getNome).toList();
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody Categoria categoria) {
        if (repository.findByNome(categoria.getNome()).isEmpty()) {
            repository.save(categoria);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{nome}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable String nome) {
        // Decodifica o nome para garantir que nomes com espaço/acentos funcionem
        String nomeDecodificado = URLDecoder.decode(nome, StandardCharsets.UTF_8);
        
        brinquedoService.excluirPorCategoria(nomeDecodificado);
        repository.deleteByNome(nomeDecodificado);
        
        return ResponseEntity.noContent().build();
    }
}
