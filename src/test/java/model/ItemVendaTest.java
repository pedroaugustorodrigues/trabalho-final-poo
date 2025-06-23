package test.java.model;

import main.java.model.Categoria;
import main.java.model.ItemVenda;
import main.java.model.Marca;
import main.java.model.Produto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemVendaTest {
    @Test
    void testSubtotal() {
        Produto p = new Produto("Notebook", "Eletrônico", "Dell", 3200, 15);
        ItemVenda item = new ItemVenda(p, 5, "15,6 polegadas", "Cinza");
        assertEquals(p, item.getProduto());
        assertEquals(5, item.getQuantidade());
        assertEquals("15,6 polegadas", item.getTamanho());
        assertEquals("Cinza", item.getCor());
        assertEquals(16000.0, item.getSubtotal());
    }
}