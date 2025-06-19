package main.java.repository;

import main.java.model.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {
    private static final String NOME_ARQUIVO = "usuarios.dat";
    private List<Usuario> usuarios;

    public UsuarioRepository() {
        this.usuarios = carregarUsuarios();
        
        int maxId = 0;
        for (Usuario u : usuarios) {
            if (u.getId() > maxId) {
                maxId = u.getId();
            }
        }
        Usuario.setProximoId(maxId); 
    }

    public void adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        salvarUsuarios();
    }

    public boolean removerUsuario(int id) {
        boolean removido = this.usuarios.removeIf(u -> u.getId() == id);
        if (removido) {
            salvarUsuarios();
        }
        return removido;
    }

    public void atualizarUsuario(Usuario usuarioAtualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuarioAtualizado.getId()) {
                usuarios.set(i, usuarioAtualizado);
                salvarUsuarios();
                return;
            }
        }
    }

    public Optional<Usuario> buscarPorId(int id) {
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

    @SuppressWarnings("unchecked")
    private List<Usuario> carregarUsuarios() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar usuários do arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários no arquivo: " + e.getMessage());
        }
    }
}
