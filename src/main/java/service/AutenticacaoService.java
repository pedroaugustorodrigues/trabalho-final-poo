package main.java.service;

import main.java.exceptions.AutenticacaoException;
import main.java.exceptions.RegistroDuplicadoException;
import main.java.model.Cliente;
import main.java.model.Gestor;
import main.java.model.Usuario;
import main.java.repository.UsuarioRepository;

/**
 * Serviço de autenticação e cadastro de usuários.
 * Responsável por login, cadastro e validação de credenciais.
 *
 * @author Pedro
 */
public class AutenticacaoService {
    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor do serviço de autenticação.
     * @param usuarioRepository repositório de usuários
     */
    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Realiza o cadastro de um novo usuário (Cliente ou Gestor).
     * @param nome Nome do usuário
     * @param cpf CPF do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @param tipo Tipo de usuário ("cliente" ou "gestor")
     * @throws RegistroDuplicadoException se o email já estiver cadastrado
     */
    public void cadastrarUsuario(String nome, String cpf, String email, String senha, String tipo) throws RegistroDuplicadoException {
        if (usuarioRepository.buscarPorEmail(email).isPresent()) {
            throw new RegistroDuplicadoException("Email já cadastrado: " + email);
        }

        Usuario novoUsuario;
        if ("cliente".equalsIgnoreCase(tipo)) {
            novoUsuario = new Cliente(nome, cpf, email, "não informado", senha);
        } else if ("gestor".equalsIgnoreCase(tipo)) {
            novoUsuario = new Gestor(nome, cpf, email, senha);
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
        usuarioRepository.adicionarUsuario(novoUsuario);
        System.out.println(tipo + " " + nome + " cadastrado com sucesso! ID: " + novoUsuario.getId());
    }

    /**
     * Realiza o login do usuário.
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return Usuário autenticado
     * @throws AutenticacaoException se email ou senha estiverem incorretos
     */
    public Usuario login(String email, String senha) throws AutenticacaoException {
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new AutenticacaoException("Email ou senha inválidos."));

        if (!usuario.getSenha().equals(senha)) {
            throw new AutenticacaoException("Email ou senha inválidos.");
        }
        return usuario;
    }

    /**
     * Autentica o usuário para operações sensíveis (ex: alteração de senha).
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return true se as credenciais estiverem corretas, false caso contrário
     */
    public boolean autenticar(String email, String senha) {
        try {
            Usuario usuario = usuarioRepository.buscarPorEmail(email)
                    .orElse(null);
            
            if (usuario == null) {
                return false;
            }
            
            return usuario.getSenha().equals(senha);
        } catch (Exception e) {
            return false;
        }
    }
}   
