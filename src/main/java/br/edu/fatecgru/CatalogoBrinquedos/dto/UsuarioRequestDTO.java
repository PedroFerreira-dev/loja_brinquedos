package br.edu.fatecgru.CatalogoBrinquedos.dto;

import br.edu.fatecgru.CatalogoBrinquedos.model.entity.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsuarioRequestDTO {
    @NotBlank(message = "Login é obrigatório")
    private String login;
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    @Email(message = "Email inválido")
    private String email;
    private String endereco;
    private Perfil perfil;

    public UsuarioRequestDTO() {}

    public UsuarioRequestDTO(String login, String senha, String nome, String cpf, String email, String endereco, Perfil perfil) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
        this.perfil = perfil;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
}
