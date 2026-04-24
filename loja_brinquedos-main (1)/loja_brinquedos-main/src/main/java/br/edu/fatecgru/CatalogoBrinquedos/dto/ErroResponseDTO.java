package br.edu.fatecgru.CatalogoBrinquedos.dto;

import java.time.LocalDateTime;

public class ErroResponseDTO {
    private int status;
    private String mensagem;
    private LocalDateTime timestamp;

    public ErroResponseDTO() {}

    public ErroResponseDTO(String mensagem, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.timestamp = timestamp;
    }

    public ErroResponseDTO(int status, String mensagem, LocalDateTime timestamp) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
