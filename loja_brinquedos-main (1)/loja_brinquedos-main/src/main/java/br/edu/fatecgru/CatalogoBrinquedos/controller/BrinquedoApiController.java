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

    // Retorna a lista completa de brinquedos
    @GetMapping
    public ResponseEntity<List<BrinquedoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // Busca detalhes de um brinquedo específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<BrinquedoRequestDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Cria um novo brinquedo (Requer perfil ADMIN)
    @PostMapping  
    public ResponseEntity<BrinquedoResponseDTO> criar(@Valid @RequestBody BrinquedoRequestDTO request) { 	
        BrinquedoResponseDTO response = service.salvarBrinquedo(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Atualiza dados de um brinquedo existente
    @PutMapping("/{id}")
    public ResponseEntity<BrinquedoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody BrinquedoRequestDTO request) {
        BrinquedoResponseDTO response = service.atualizarBrinquedo(id, request);
        return ResponseEntity.ok(response);
    }

    // Remove um brinquedo por ID
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> excluir(@PathVariable Long id) { 
        service.excluirBrinquedo(id);
        return ResponseEntity.noContent().build();
    }

    // Remove todos os brinquedos de uma categoria específica
    @DeleteMapping("/categoria/{categoria}")
    public ResponseEntity<Void> excluirPorCategoria(@PathVariable String categoria) {
        service.excluirPorCategoria(categoria);
        return ResponseEntity.noContent().build();
    }

    // Filtra brinquedos por categoria
    @GetMapping("/filtro")
    public ResponseEntity<List<BrinquedoResponseDTO>> filtrarPorCategoria(@RequestParam("categoria") String categoria) {
        return ResponseEntity.ok(service.buscarPorCategoria(categoria));
    }

    // Lista todas as categorias cadastradas sem duplicidade
    @GetMapping("/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        return ResponseEntity.ok(service.listarCategorias());
    }

    // Retorna os itens recém-adicionados (Novidades)
    @GetMapping("/novidades")
    public ResponseEntity<List<BrinquedoResponseDTO>> listarNovidades() {
        return ResponseEntity.ok(service.listarNovidades());
    }
    
    // Registra um novo pedido (Exige autenticação)
    @PostMapping("/encomendar/{id}")
    public ResponseEntity<EncomendaResponseDTO> encomendar(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioService.buscarEntidadePorLogin(auth.getName());
        return ResponseEntity.ok(service.encomendarBrinquedo(id, usuario));
    }

    // Lista histórico de pedidos do usuário autenticado
    @GetMapping("/historico")
    public ResponseEntity<List<EncomendaResponseDTO>> listarHistorico(Authentication auth) {
        Usuario usuario = usuarioService.buscarEntidadePorLogin(auth.getName());
        return ResponseEntity.ok(service.listarHistorico(usuario));
    }

    // Lista todos os pedidos do sistema (Requer perfil ADMIN)
    @GetMapping("/encomendas/todas")
    public ResponseEntity<List<EncomendaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // Confirmação de recebimento pelo cliente
    @PostMapping("/encomendar/{id}/confirmar")
    public ResponseEntity<EncomendaResponseDTO> confirmarEntrega(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioService.buscarEntidadePorLogin(auth.getName());
        return ResponseEntity.ok(service.clienteConfirmarEntrega(id, usuario));
    }

    // Alteração de status do pedido pelo administrador
    @PatchMapping("/encomendar/{id}/status")
    public ResponseEntity<EncomendaResponseDTO> atualizarStatus(
            @PathVariable Long id, 
            @RequestParam br.edu.fatecgru.CatalogoBrinquedos.model.entity.StatusEncomenda novoStatus) {
        return ResponseEntity.ok(service.adminAlterarStatus(id, novoStatus));
    }
        
}
