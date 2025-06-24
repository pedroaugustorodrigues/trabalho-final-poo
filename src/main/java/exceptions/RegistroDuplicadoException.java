package main.java.exceptions;

/**
 * Exceção lançada quando há tentativa de cadastro duplicado.
 *
 * @author Pedro
 */
public class RegistroDuplicadoException extends Exception {
    /**
     * Cria uma nova exceção de registro duplicado com a mensagem informada.
     * @param message mensagem de erro
     */
    public RegistroDuplicadoException(String message) {
        super(message);
    }
}
