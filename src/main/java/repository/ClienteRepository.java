package main.java.repository;

import main.java.model.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório de clientes.
 * Responsável por persistir, buscar, atualizar e remover clientes do sistema.
 *
 * @author Rafael
 */
public class ClienteRepository {
    private final String arquivo = "clientes.dat";
    private final List<Cliente> clientes;

    /**
     * Construtor padrão. Carrega os clientes do arquivo.
     */
    public ClienteRepository() {
        clientes = carregarDoArquivo();
    }

    /**
     * Adiciona um novo cliente e salva no arquivo.
     */
    public void adicionar(Cliente cliente) {
        clientes.add(cliente);
        salvarNoArquivo();
    }

    /**
     * Atualiza um cliente existente e salva no arquivo.
     */
    public void atualizar(Cliente clienteAtualizado) {
        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            if (c.getCpf().equals(clienteAtualizado.getCpf())) {
                clientes.set(i, clienteAtualizado);
                salvarNoArquivo();
                return;
            }
        }
    }

    /**
     * Remove um cliente pelo CPF e salva no arquivo.
     */
    public void removerPorCpf(String cpf) {
        clientes.removeIf(c -> c.getCpf().equals(cpf));
        salvarNoArquivo();
    }

    /**
     * Busca um cliente pelo CPF.
     */
    public Cliente buscarPorCpf(String cpf) {
        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null;
    }

    /**
     * Lista todos os clientes.
     */
    public List<Cliente> listar() {
        return clientes;
    }

    /**
     * Salva a lista de clientes no arquivo.
     */
    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de clientes do arquivo.
     */
    @SuppressWarnings("unchecked")
    private List<Cliente> carregarDoArquivo() {
        File file = new File(arquivo);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Cliente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
