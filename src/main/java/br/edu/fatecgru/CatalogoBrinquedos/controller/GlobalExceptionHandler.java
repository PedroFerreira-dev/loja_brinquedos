package br.edu.fatecgru.CatalogoBrinquedos.controller;

import br.edu.fatecgru.CatalogoBrinquedos.dto.ErroResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Tratativa para "Não Encontrado" (404)
    @ExceptionHandler(RuntimeException.class) // Por enquanto capturamos Runtime, mas podemos ser mais específicos
    public ResponseEntity<ErroResponseDTO> tratarObjetoNaoEncontrado(RuntimeException ex, HttpServletRequest request) {
        
        // Se a mensagem contiver "não encontrado", mandamos 404, senão 400 (Bad Request)
        HttpStatus status = ex.getMessage().contains("não encontrado") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(erro);
    }
}