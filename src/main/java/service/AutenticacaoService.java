package main.java.service;

import main.java.exceptions.AutenticacaoException;
import main.java.exceptions.RegistroDuplicadoException;
import main.java.model.Cliente;
import main.java.model.Gestor;
import main.java.model.Usuario;
import main.java.repository.UsuarioRepository;

public class AutenticacaoService {
    private UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void cadastrarUsuario(String nome, String email, String senha, String tipo) throws RegistroDuplicadoException {
        if (usuarioRepository.buscarPorEmail(email).isPresent()) {
            throw new RegistroDuplicadoException("Email já cadastrado: " + email);
        }

        Usuario novoUsuario;
        if ("cliente".equalsIgnoreCase(tipo)) {
            novoUsuario = new Cliente(nome, email, senha);
        } else if ("gestor".equalsIgnoreCase(tipo)) {
            novoUsuario = new Gestor(nome, email, senha);
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
        usuarioRepository.adicionarUsuario(novoUsuario);
        System.out.println(tipo + " " + nome + " cadastrado com sucesso! ID: " + novoUsuario.getId());
    }

    public Usuario login(String email, String senha) throws AutenticacaoException {
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new AutenticacaoException("Email ou senha inválidos."));

        if (!usuario.getSenha().equals(senha)) {
            throw new AutenticacaoException("Email ou senha inválidos.");
        }
        return usuario;
    }

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
