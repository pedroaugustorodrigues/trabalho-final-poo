package test.java.model;

import main.java.model.Cliente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {
    @Test
    void testCriacaoCliente() {
        Cliente c = new Cliente("João", "12345678912", "joao@email.com", "999999999", "1234");
        assertEquals("João", c.getNome());
        assertEquals("12345678912", c.getCpf());
        assertEquals("joao@email.com", c.getEmail());
        assertEquals("999999999", c.getCelular());
        assertEquals("1234", c.getSenha());
    }
}