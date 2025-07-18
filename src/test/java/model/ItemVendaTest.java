package test.java.model;

import main.java.model.ItemVenda;
import main.java.model.Produto;
import main.java.model.Categoria;
import main.java.model.Marca;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemVendaTest {
    @Test
    void testSubtotal() {
        Produto p = new Produto("Notebook", new Categoria("Eletrônico"), new Marca("Dell"), 3200, 15);
        ItemVenda item = new ItemVenda(p, 5);
        assertEquals(p, item.getProduto());
        assertEquals(5, item.getQuantidade());
        assertEquals(16000.0, item.getSubtotal());
    }
}