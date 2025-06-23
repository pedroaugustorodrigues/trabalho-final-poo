package test.java.model;

import main.java.model.Gestor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GestorTest {
    @Test
    void testCriacaoGestor() {
        Gestor g = new Gestor("admin", "admin@123", "1234");
        assertEquals("admin", g.getNome());
        assertEquals("admin@123", g.getEmail());
        assertEquals("1234", g.getSenha());
    }
}