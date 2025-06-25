package test.java.repository;

import main.java.model.Categoria;
import main.java.repository.CategoriaRepository;

import java.util.ArrayList;
import java.util.List;

public class CategoriaRepositoryEmMemoria extends CategoriaRepository {
    private List<Categoria> categoriasInMemory = new ArrayList<>();

    protected List<Categoria> carregarDoArquivo() {
        return categoriasInMemory;
    }

    protected void salvarNoArquivo() {
    }
}