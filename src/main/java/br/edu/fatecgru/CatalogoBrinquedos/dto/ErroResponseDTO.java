package br.edu.fatecgru.CatalogoBrinquedos.dto;

import java.time.LocalDateTime;

public record ErroResponseDTO( // DTO para padronizar as respostas de erro. record é uma classe imutável, ideal para esse tipo de uso.
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem,
    String caminho
) {}