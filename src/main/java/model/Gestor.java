package main.java.model;

public class Gestor extends Usuario {
    private static final long serialVersionUID = 1L;
    private String cpf;

    public Gestor(String nome, String cpf, String email, String senha) {
        super(nome, email, senha);
        this.cpf = cpf;
    }

    @Override
    public String getTipoUsuario() {
        return "Gestor";
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
