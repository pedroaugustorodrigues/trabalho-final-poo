package main.java.model;

public class Gestor extends Usuario {
    private static final long serialVersionUID = 1L;

    public Gestor(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public String getTipoUsuario() {
        return "Gestor";
    }
}
