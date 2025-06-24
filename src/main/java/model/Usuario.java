package main.java.model;

import java.io.Serializable;

/**
 * Classe base para todos os usuários do sistema.
 */
public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int proximoId = 1;

    private int id;
    private String nome;
    private String email;
    private String senha;

    /**
     * Cria um novo usuário com nome, email e senha.
     */
    public Usuario(String nome, String email, String senha) {
        this.id = proximoId++;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    /**
     * Retorna o tipo do usuário (Cliente ou Gestor).
     */
    public abstract String getTipoUsuario();

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Email: " + email;
    }

    /**
     * Reseta o contador de IDs (usado em testes).
     */
    public static void resetProximoId() { proximoId = 1; }

    /**
     * Atualiza o próximo ID a ser usado (usado em repositórios).
     */
    public static void setProximoId(int ultimoId) {
        if (ultimoId >= proximoId) {
            proximoId = ultimoId + 1;
        }
    }
}
