package test.java.repository;

import main.java.repository.UsuarioRepository;

import java.io.File;

public class UsuarioRepositoryEmMemoria extends UsuarioRepository {
    public UsuarioRepositoryEmMemoria() {
        super("usuarios_test.dat");
    }

    public static void limparArquivoTeste() {
        File f = new File("usuarios_test.dat");
        if (f.exists()) f.delete();
    }
}