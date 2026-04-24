package br.edu.fatecgru.CatalogoBrinquedos.dto;

import java.time.LocalDateTime;

public class EncomendaResponseDTO {
    private Long id;
    private String nomeBrinquedo;
    private String categoriaBrinquedo;
    private Double precoNaData;
    private LocalDateTime dataPedido;
    private String status;

    public EncomendaResponseDTO() {}

    public EncomendaResponseDTO(Long id, String nomeBrinquedo, String categoriaBrinquedo, Double precoNaData, LocalDateTime dataPedido, String status) {
        this.id = id;
        this.nomeBrinquedo = nomeBrinquedo;
        this.categoriaBrinquedo = categoriaBrinquedo;
        this.precoNaData = precoNaData;
        this.dataPedido = dataPedido;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeBrinquedo() { return nomeBrinquedo; }
    public void setNomeBrinquedo(String nomeBrinquedo) { this.nomeBrinquedo = nomeBrinquedo; }
    public String getCategoriaBrinquedo() { return categoriaBrinquedo; }
    public void setCategoriaBrinquedo(String categoriaBrinquedo) { this.categoriaBrinquedo = categoriaBrinquedo; }
    public Double getPrecoNaData() { return precoNaData; }
    public void setPrecoNaData(Double precoNaData) { this.precoNaData = precoNaData; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
