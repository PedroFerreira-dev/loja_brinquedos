package br.edu.fatecgru.CatalogoBrinquedos.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoRequestDTO;
import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoResponseDTO;
import br.edu.fatecgru.CatalogoBrinquedos.dto.EncomendaResponseDTO;
import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Usuario;
import br.edu.fatecgru.CatalogoBrinquedos.service.BrinquedoService;
import br.edu.fatecgru.CatalogoBrinquedos.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/brinquedos")
public class BrinquedoApiController {

    @Autowired
    private BrinquedoService service;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<BrinquedoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrinquedoRequestDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping  
    public ResponseEntity<BrinquedoResponseDTO> criar(@Valid @RequestBody BrinquedoRequestDTO request) { 	
        BrinquedoResponseDTO response = service.salvarBrinquedo(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrinquedoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody BrinquedoRequestDTO request) {
        BrinquedoResponseDTO response = service.atualizarBrinquedo(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> excluir(@PathVariable Long id) { 
        service.excluirBrinquedo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<BrinquedoResponseDTO>> buscarPorNome(@RequestParam("termo") String termo) {
        return ResponseEntity.ok(service.buscarPorTermo(termo));
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<BrinquedoResponseDTO>> filtrarPorCategoria(@RequestParam("categoria") String categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        return ResponseEntity.ok(service.listarCategorias());
    }

    @GetMapping("/ordenar")
    public ResponseEntity<List<BrinquedoResponseDTO>> ordenarPorPreco(@RequestParam(defaultValue = "asc") String sentido) {
        return ResponseEntity.ok(service.listarPorPreco(sentido));
    }

    @GetMapping("/novidades")
    public ResponseEntity<List<BrinquedoResponseDTO>> listarNovidades() {
        return ResponseEntity.ok(service.listarNovidades());
    }
    
    @GetMapping("/destaques")
    public ResponseEntity<List<BrinquedoResponseDTO>> listarDestaques() {
        return ResponseEntity.ok(service.listarDestaques());
    }
    
    // NOVO: Encomendar agora exige usuário logado
    @PostMapping("/encomendar/{id}")
    public ResponseEntity<EncomendaResponseDTO> encomendar(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioService.buscarEntidadePorLogin(auth.getName());
        return ResponseEntity.ok(service.encomendarBrinquedo(id, usuario));
    }

    // NOVO: Histórico de encomendas do usuário logado
    @GetMapping("/historico")
    public ResponseEntity<List<EncomendaResponseDTO>> listarHistorico(Authentication auth) {
        Usuario usuario = usuarioService.buscarEntidadePorLogin(auth.getName());
        return ResponseEntity.ok(service.listarHistorico(usuario));
    }

    // NOVO: Admin lista todas as encomendas do sistema
    @GetMapping("/encomendas/todas")
    public ResponseEntity<List<EncomendaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // NOVO: Cliente confirma entrega
    @PostMapping("/encomendar/{id}/confirmar")
    public ResponseEntity<EncomendaResponseDTO> confirmarEntrega(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioService.buscarEntidadePorLogin(auth.getName());
        return ResponseEntity.ok(service.clienteConfirmarEntrega(id, usuario));
    }

    // NOVO: Admin altera status (ex: para ENVIADO)
    @PatchMapping("/encomendar/{id}/status")
    public ResponseEntity<EncomendaResponseDTO> atualizarStatus(
            @PathVariable Long id, 
            @RequestParam br.edu.fatecgru.CatalogoBrinquedos.model.entity.StatusEncomenda novoStatus) {
        return ResponseEntity.ok(service.adminAlterarStatus(id, novoStatus));
    }

    @PatchMapping("/repor/{id}")
    public ResponseEntity<BrinquedoResponseDTO> reporEstoque(
            @PathVariable Long id, 
            @RequestParam Integer quantidade) {
        return ResponseEntity.ok(service.reporEstoque(id, quantidade));
    }
        
}
