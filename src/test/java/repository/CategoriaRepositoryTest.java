package test.java.repository;

import main.java.model.Categoria;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaRepositoryTest {

    @Test
    void testAdicionarCategoriaEmMemoria() {
        CategoriaRepositoryEmMemoria repo = new CategoriaRepositoryEmMemoria();
        Categoria cat = new Categoria("Informática");

        repo.adicionar(cat);
        List<Categoria> categorias = repo.listar();

        assertTrue(categorias.contains(cat));
    }
}