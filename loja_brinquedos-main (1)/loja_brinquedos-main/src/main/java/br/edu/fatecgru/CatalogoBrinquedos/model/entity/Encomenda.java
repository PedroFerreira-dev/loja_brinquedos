package br.edu.fatecgru.CatalogoBrinquedos.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Encomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @JoinColumn(name = "brinquedo_id", nullable = false)
    private Brinquedo brinquedo;

    private LocalDateTime dataPedido;

    private Double precoNaData;

    @Enumerated(EnumType.STRING)
    private StatusEncomenda status;

    public Encomenda() {
        this.dataPedido = LocalDateTime.now();
        this.status = StatusEncomenda.PROCESSANDO;
    }

    public Encomenda(Usuario usuario, Brinquedo brinquedo, Double precoNaData) {
        this();
        this.usuario = usuario;
        this.brinquedo = brinquedo;
        this.precoNaData = precoNaData;
    }

    // Getters e Setters (adicionar o do status)
    public StatusEncomenda getStatus() {
        return status;
    }

    public void setStatus(StatusEncomenda status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Brinquedo getBrinquedo() {
        return brinquedo;
    }

    public void setBrinquedo(Brinquedo brinquedo) {
        this.brinquedo = brinquedo;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Double getPrecoNaData() {
        return precoNaData;
    }

    public void setPrecoNaData(Double precoNaData) {
        this.precoNaData = precoNaData;
    }
}
