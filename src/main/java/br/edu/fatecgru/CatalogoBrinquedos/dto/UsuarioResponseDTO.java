package br.edu.fatecgru.CatalogoBrinquedos.dto;

import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Perfil;

public class UsuarioResponseDTO {
    private Long id;
    private String login;
    private String nome;
    private String cpf;
    private String email;
    private String endereco;
    private String perfil;

    public UsuarioResponseDTO() {}

    public UsuarioResponseDTO(Long id, String login, String nome, String cpf, String email, String endereco, Perfil perfil) {
        this.id = id;
        this.login = login;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
        this.perfil = perfil != null ? perfil.name().replace("ROLE_", "") : "USER";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
}
