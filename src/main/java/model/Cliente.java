package main.java.model;

public class Cliente extends Usuario {
    private static final long serialVersionUID = 1L;

    private String cpf;
    private String celular;

    public Cliente(String nome, String cpf, String email, String celular, String senha) {
        super(nome, email, senha);
        this.cpf = cpf;
        this.celular = celular;
    }

    public Cliente(String nome, String email, String senha) {
        super(nome, email, senha);
        this.cpf = "não informado";
        this.celular = "não informado";
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public String getTipoUsuario() {
        return "Cliente";
    }

    @Override
    public String toString() {
        return getNome() + " - CPF: " + cpf;
    }
}
