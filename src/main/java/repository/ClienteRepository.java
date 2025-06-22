package main.java.repository;

import main.java.model.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private final String arquivo = "clientes.dat";
    private List<Cliente> clientes;

    public ClienteRepository() {
        clientes = carregarDoArquivo();
    }

    public void adicionar(Cliente cliente) {
        clientes.add(cliente);
        salvarNoArquivo();
    }

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

    public void removerPorCpf(String cpf) {
        clientes.removeIf(c -> c.getCpf().equals(cpf));
        salvarNoArquivo();
    }

    public List<Cliente> listar() {
        return clientes;
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
