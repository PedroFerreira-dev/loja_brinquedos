package br.edu.fatecgru.CatalogoBrinquedos.controller;

import br.edu.fatecgru.CatalogoBrinquedos.dto.UsuarioRequestDTO;
import br.edu.fatecgru.CatalogoBrinquedos.dto.UsuarioResponseDTO;
import br.edu.fatecgru.CatalogoBrinquedos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioApiController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO request) {
        return new ResponseEntity<>(service.cadastrar(request), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> meuPerfil(Authentication auth) {
        return ResponseEntity.ok(service.buscarPorLogin(auth.getName()));
    }

    // Endpoint para o frontend validar o login (Basic Auth fará o resto)
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(Authentication auth) {
        return ResponseEntity.ok(service.buscarPorLogin(auth.getName()));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> desativarConta(Authentication auth) {
        service.desativarConta(auth.getName());
        return ResponseEntity.noContent().build();
    }
}
