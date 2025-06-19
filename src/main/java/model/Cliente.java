package main.java.model;

public class Cliente extends Usuario {
    private static final long serialVersionUID = 1L;

    public Cliente(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public String getTipoUsuario() {
        return "Cliente";
    }
}
