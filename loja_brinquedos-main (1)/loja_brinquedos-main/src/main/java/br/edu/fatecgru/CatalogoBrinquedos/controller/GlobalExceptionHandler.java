package br.edu.fatecgru.CatalogoBrinquedos.controller;

import br.edu.fatecgru.CatalogoBrinquedos.dto.ErroResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Trata erros de lógica de negócio e exceções gerais
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponseDTO> handleRuntimeException(RuntimeException ex) {
        String msg = ex.getMessage();
        
        // Diferencia erros de integridade (exclusão vs inserção)
        if (msg.contains("DataIntegrityViolationException") || msg.contains("ConstraintViolationException")) {
            if (msg.toLowerCase().contains("delete") || msg.toLowerCase().contains("fk")) {
                msg = "Não é possível excluir: este item possui pedidos ou vínculos ativos.";
            } else {
                msg = "Erro ao salvar: verifique se os dados estão corretos ou se já existe um registro similar.";
            }
        }

        ErroResponseDTO erro = new ErroResponseDTO(msg, LocalDateTime.now());
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    // Trata erros de validação de campos (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
