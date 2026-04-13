package br.edu.fatecgru.CatalogoBrinquedos.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity // Avisa o banco que isso vai virar uma tabela
public class Brinquedo {

    @Id // Avisa que esse é o ID (Chave Primária)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Avisa para o banco gerar os números sozinho (1, 2, 3...)
    private Long id;
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome; // Campo para armazenar o nome do brinquedo
    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero.")
    private Double preco; // Campo para armazenar o preço do brinquedo
    @NotBlank(message = "A categoria é obrigatória")
    private String categoria; // Campo para armazenar a categoria do brinquedo (ex: "Ação", "Educativo", "Boneca", etc.)
    @NotBlank(message = "O caminho da imagem é obrigatório")
    private String caminhoImagem; // Campo para armazenar o caminho da imagem do brinquedo (pode ser uma URL ou um caminho local)
    private Double desconto; // Campo para controlar o desconto aplicado ao brinquedo (em porcentagem, ex: 10.0 para 10% de desconto)
    @NotNull(message = "A quantidade em estoque é obrigatória")
    @PositiveOrZero(message = "O estoque não pode ser negativo")
    private Integer quantidade; // Campo para controlar a quantidade disponível do brinquedo
    private Integer vendas = 0; // Campo para controlar o número de vendas do brinquedo
    @NotBlank(message = "A descrição não pode estar vazia")
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

	public Double getDesconto() {
	    // Se o banco de dados devolver nulo, nós garantimos que o sistema não quebre
	    // e retorne 0.0 (sem desconto) por padrão.
	    if (this.desconto == null) {
	        return 0.0;
	    }
	    return this.desconto;
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
