package br.edu.fatecgru.CatalogoBrinquedos.dto;

public class BrinquedoResponseDTO {

    private Long id;
    private String nome;
    private Double preco;
    private String categoria;
    private String caminhoImagem;
    private Double desconto;
    private Integer quantidade;
    private String descricao;

    public BrinquedoResponseDTO() {
    }

    // Criando um construtor inteligente que já converte a entidade para DTO
    public BrinquedoResponseDTO(Long id, String nome, Double preco, String categoria, String caminhoImagem,
			Double desconto, Integer quantidade, String descricao) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
		this.categoria = categoria;
		this.caminhoImagem = caminhoImagem;
		this.desconto = desconto;
		this.quantidade = quantidade;
		this.descricao = descricao;
	}

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
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
    
}