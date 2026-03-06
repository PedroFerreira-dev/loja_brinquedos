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
public class BrinquedoApiController {

    @Autowired
    private BrinquedoService service;

    // 1. LISTAGEM GERAL
    @GetMapping
    public ResponseEntity<List<BrinquedoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // 2. BUSCA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<BrinquedoRequestDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // 3. CRIAR NOVO (POST)
    @PostMapping  
    public ResponseEntity<BrinquedoResponseDTO> criar(@RequestBody BrinquedoRequestDTO request) { 	
        BrinquedoResponseDTO response = service.salvarBrinquedo(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 4. ATUALIZAR EXISTENTE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<BrinquedoResponseDTO> atualizar(@PathVariable Long id, @RequestBody BrinquedoRequestDTO request) {
        BrinquedoResponseDTO response = service.atualizarBrinquedo(id, request);
        return ResponseEntity.ok(response);
    }

    // 5. EXCLUIR
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> excluir(@PathVariable Long id) { 
        service.excluirBrinquedo(id);
        return ResponseEntity.noContent().build();
    }

    // 6. FILTRO POR NOME
    @GetMapping("/buscar")
    public ResponseEntity<List<BrinquedoResponseDTO>> buscarPorNome(@RequestParam("termo") String termo) {
        return ResponseEntity.ok(service.buscarPorNome(termo));
    }

    // 7. FILTRO POR CATEGORIA
    @GetMapping("/filtro")
    public ResponseEntity<List<BrinquedoResponseDTO>> filtrarPorCategoria(@RequestParam("categoria") String categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

    // 8. LISTA ÚNICA DE CATEGORIAS (Para o seu colega do Front!)
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        return ResponseEntity.ok(service.listarCategorias());
    }

    // 9. ORDENAÇÃO POR PREÇO (asc ou desc)
    @GetMapping("/ordenar")
    public ResponseEntity<List<BrinquedoResponseDTO>> ordenarPorPreco(@RequestParam(defaultValue = "asc") String sentido) {
        return ResponseEntity.ok(service.listarPorPreco(sentido));
    }

    // 10. NOVIDADES (Os últimos 3)
    @GetMapping("/novidades")
    public ResponseEntity<List<BrinquedoResponseDTO>> listarNovidades() {
        return ResponseEntity.ok(service.listarNovidades());
    }
    
 // GET /api/brinquedos/destaques
    @GetMapping("/destaques")
    public ResponseEntity<List<BrinquedoResponseDTO>> listarDestaques() {
        return ResponseEntity.ok(service.listarDestaques());
    }

    // POST /api/brinquedos/encomendar/5
    @PostMapping("/encomendar/{id}")
    public ResponseEntity<BrinquedoResponseDTO> encomendar(@PathVariable Long id) {
        return ResponseEntity.ok(service.encomendarBrinquedo(id));
    }

    // PATCH /api/brinquedos/repor/5?quantidade=10
    @PatchMapping("/repor/{id}")
    public ResponseEntity<BrinquedoResponseDTO> reporEstoque(
            @PathVariable Long id, 
            @RequestParam Integer quantidade) {
        return ResponseEntity.ok(service.reporEstoque(id, quantidade));
    }
        
}