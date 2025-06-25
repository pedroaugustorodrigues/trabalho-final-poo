package test.java.model;

import main.java.model.Marca;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MarcaTest {
    @Test
    void testCriacaoMarca() {
        Marca marca = new Marca("Samsung");
        assertEquals("Samsung", marca.getNome());
    }
}
