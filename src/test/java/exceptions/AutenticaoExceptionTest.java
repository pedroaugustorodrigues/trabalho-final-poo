package test.java.exceptions;

import main.java.exceptions.AutenticacaoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoExceptionTest {

    @Test
    void testConstrutorMensagem() {
        String mensagem = "Erro de autenticação";
        AutenticacaoException ex = new AutenticacaoException(mensagem);

        assertEquals(mensagem, ex.getMessage());
    }
}