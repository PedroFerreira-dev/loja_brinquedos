package br.edu.fatecgru.CatalogoBrinquedos.model.entity;

public enum StatusEncomenda {
    PROCESSANDO, // Inicial
    ENVIADO,     // Alterado pelo Admin
    ENTREGUE     // Confirmado pelo Cliente
}
