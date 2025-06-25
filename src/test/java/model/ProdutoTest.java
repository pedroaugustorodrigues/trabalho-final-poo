package test.java.model;

import main.java.model.Produto;
import main.java.model.Categoria;
import main.java.model.Marca;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {
    @Test
    void testCriacaoProduto() {
        Produto produto = new Produto("TV", new Categoria("Eletrônico"), new Marca("Samsung"), 2000, 10);
        assertEquals("TV", produto.getDescricao());
        assertEquals("Eletrônico", produto.getCategoria().getNome());
        assertEquals("Samsung", produto.getMarca().getNome());
        assertEquals(2000, produto.getPreco());
        assertEquals(10, produto.getQtd());
    }
}