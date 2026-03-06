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

    @Enumerated(EnumType.STRING) // Armazena o perfil como string no banco (ex: "ROLE_ADMIN", "ROLE_USER")
    private Perfil perfil;

    public Usuario() {} // Construtor padrão necessário para JPA

    public Usuario(String login, String senha, Perfil perfil) { // Construtor para facilitar a criação de usuários
        this.login = login;
        this.senha = senha;
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

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

    
}