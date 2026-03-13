package br.edu.fatecgru.CatalogoBrinquedos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // Avisa o banco que isso vai virar uma tabela
public class Brinquedo {

    @Id // Avisa que esse é o ID (Chave Primária)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Avisa para o banco gerar os números sozinho (1, 2, 3...)
    private Long id;
    
    private String nome; // Descrição/Nome do brinquedo [cite: 88, 106]
    private String marca; // Campo adicionado para bater com o Admin 
    private Double preco; // Valor do brinquedo [cite: 94, 112]
    private String categoria; // Categoria (ex: Heróis, Meninas, Bebês) [cite: 90, 108]
    private String caminhoImagem; // URL da imagem [cite: 93, 111]
    private Double desconto; // Porcentagem de desconto
    private Integer quantidade; // Controle de estoque
    private Integer vendas; // Contador de vendas para destaques
    
    @Column(columnDefinition = "TEXT") // Define como texto longo no banco
    private String descricao; // Detalhes detalhados do brinquedo 
   
    // Construtor vazio
    public Brinquedo() {
    }

    // Construtor completo (Atualizado com 'marca' e 'descricao')
    public Brinquedo(Long id, String nome, String marca, Double preco, String categoria, String caminhoImagem, 
                     Double desconto, Integer quantidade, Integer vendas, String descricao) {
        this.id = id;
        this.nome = nome;
        this.marca = marca;
        this.preco = preco;
        this.categoria = categoria;
        this.caminhoImagem = caminhoImagem;
        this.desconto = desconto;
        this.quantidade = quantidade;
        this.vendas = vendas;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    public Integer getVendas() {
        return vendas;
    }
    
    public void setVendas(Integer vendas) {
        this.vendas = vendas;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}