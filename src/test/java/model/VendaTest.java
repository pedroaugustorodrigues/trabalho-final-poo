package test.java.model;

import main.java.model.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendaTest {

    @Test
    void testCalculoTotalVenda() {
        Produto celular = new Produto("Celular", "Eletrônicos", "Samsung", 1500.0, 10);
        Produto fone = new Produto("Fone", "Eletrônicos", "JBL", 500.0, 20);

        ItemVenda item1 = new ItemVenda(celular, 1, "Único", "Preto");
        ItemVenda item2 = new ItemVenda(fone, 2, "Único", "Branco");

        List<ItemVenda> itens = Arrays.asList(item1, item2);

        Cliente cliente = new Cliente("João", "123.456.789-00", "joao@email.com", "99999-9999", "senha123");

        Venda venda = new Venda(cliente, itens);

        assertEquals(2500.0, venda.getTotal(), 0.001);
    }
}