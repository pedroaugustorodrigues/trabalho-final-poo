package test.java.model;

import main.java.model.Categoria;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {
    @Test
    void testCriacaoCategoria() {
        Categoria cat = new Categoria("Eletrônicos");
        assertEquals("Eletrônicos", cat.getNome());
    }
}