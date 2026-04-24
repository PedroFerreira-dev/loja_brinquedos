package br.edu.fatecgru.CatalogoBrinquedos.model.entity;

import jakarta.persistence.*;

@Entity // Anotação para indicar que esta classe é uma entidade JPA, mapeada para uma tabela no banco de dados
public class Usuario {

    @Id // Chave primária auto-gerada
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // O login deve ser único e não pode ser nulo
    private String login;

    @Column(nullable = false) // A senha não pode ser nula
    private String senha;

    private String nome;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    private String endereco;

    @Enumerated(EnumType.STRING) // Armazena o perfil como string no banco (ex: "ROLE_ADMIN", "ROLE_USER")
    private Perfil perfil;

    private boolean ativo = true;

    public Usuario() {} // Construtor padrão necessário para JPA

    public Usuario(String login, String senha, String nome, String cpf, String email, String endereco, Perfil perfil) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
        this.perfil = perfil;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    
}