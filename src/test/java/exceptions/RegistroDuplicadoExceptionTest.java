package test.java.exceptions;

import main.java.exceptions.RegistroDuplicadoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistroDuplicadoExceptionTest {

    @Test
    void testConstrutorMensagem() {
        String mensagem = "Registro duplicado detectado";
        RegistroDuplicadoException ex = new RegistroDuplicadoException(mensagem);

        assertEquals(mensagem, ex.getMessage());
    }
}