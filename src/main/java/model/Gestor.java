package main.java.model;

/**
 * Representa um gestor do sistema.
 */
public class Gestor extends Usuario {
    private static final long serialVersionUID = 1L;
    private String cpf;

    /**
     * Cria um gestor com todos os dados obrigatórios.
     */
    public Gestor(String nome, String cpf, String email, String senha) {
        super(nome, email, senha);
        this.cpf = cpf;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    @Override
    public String getTipoUsuario() { return "Gestor"; }
}
