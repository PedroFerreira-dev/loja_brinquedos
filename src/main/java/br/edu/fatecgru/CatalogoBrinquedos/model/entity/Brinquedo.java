package br.edu.fatecgru.CatalogoBrinquedos.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // Avisa o banco que isso vai virar uma tabela
public class Brinquedo {

    @Id // Avisa que esse é o ID (Chave Primária)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Avisa para o banco gerar os números sozinho (1, 2, 3...)
    private Long id;
    
    private String nome; // Campo para armazenar o nome do brinquedo
    private Double preco; // Campo para armazenar o preço do brinquedo
    private String categoria; // Campo para armazenar a categoria do brinquedo (ex: "Ação", "Educativo", "Boneca", etc.)
    private String caminhoImagem; // Campo para armazenar o caminho da imagem do brinquedo (pode ser uma URL ou um caminho local)
    private Double desconto; // Campo para controlar o desconto aplicado ao brinquedo (em porcentagem, ex: 10.0 para 10% de desconto)
    private Integer quantidade; // Campo para controlar a quantidade disponível do brinquedo
    private Integer vendas; // Campo para controlar o número de vendas do brinquedo
    private String descricao; // Campo para armazenar uma descrição detalhada do brinquedo (opcional)
   
	// Construtor vazio
    public Brinquedo() {
    }

	public Brinquedo(Long id, String nome, Double preco, String categoria, String caminhoImagem, double desconto,
			Integer quantidade, Integer vendas) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
		this.categoria = categoria;
		this.caminhoImagem = caminhoImagem;
		this.desconto = desconto;
		this.quantidade = quantidade;
		this.vendas = vendas;
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

	public double getDesconto() {
		return desconto;
	}

	public void setDesconto(double desconto) {
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
