package main.java.exceptions;

/**
 * Exceção lançada em caso de falha de autenticação de usuário.
 *
 * @author Pedro
 */
public class AutenticacaoException extends Exception {
    /**
     * Cria uma nova exceção de autenticação com a mensagem informada.
     * @param message mensagem de erro
     */
    public AutenticacaoException(String message) {
        super(message);
    }
}
