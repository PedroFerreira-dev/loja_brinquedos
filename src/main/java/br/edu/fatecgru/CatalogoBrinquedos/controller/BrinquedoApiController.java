package br.edu.fatecgru.CatalogoBrinquedos.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoRequestDTO;
import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoResponseDTO;
import br.edu.fatecgru.CatalogoBrinquedos.service.BrinquedoService;

@RestController
@RequestMapping("/api/brinquedos")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BrinquedoApiController {

    @Autowired
    private BrinquedoService service;

    @GetMapping
    public ResponseEntity<List<BrinquedoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrinquedoRequestDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping  
    public ResponseEntity<BrinquedoResponseDTO> criar(@RequestBody BrinquedoRequestDTO request) {     
        return new ResponseEntity<>(service.salvarBrinquedo(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrinquedoResponseDTO> atualizar(@PathVariable Long id, @RequestBody BrinquedoRequestDTO request) {
        return ResponseEntity.ok(service.atualizarBrinquedo(id, request));
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> excluir(@PathVariable Long id) { 
        service.excluirBrinquedo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/marcas")
    public List<String> listarMarcas() {
        return service.listarMarcas();
    }

    @GetMapping("/categorias")
    public List<String> listarCategorias() {
        return service.listarCategorias();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<BrinquedoResponseDTO>> buscarPorNome(@RequestParam String termo) {
        return ResponseEntity.ok(service.buscarPorNome(termo));
    }

    @GetMapping("/novidades")
    public ResponseEntity<List<BrinquedoResponseDTO>> listarNovidades() {
        return ResponseEntity.ok(service.listarNovidades());
    }

    @GetMapping("/destaques")
    public ResponseEntity<List<BrinquedoResponseDTO>> listarDestaques() {
        return ResponseEntity.ok(service.listarDestaques());
    }

    @PostMapping("/encomendar/{id}")
    public ResponseEntity<BrinquedoResponseDTO> encomendar(@PathVariable Long id) {
        return ResponseEntity.ok(service.encomendarBrinquedo(id));
    }
}