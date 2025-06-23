package test.java.repository;

import main.java.model.Cliente;
import main.java.repository.ClienteRepository;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryEmMemoria extends ClienteRepository {
    private List<Cliente> clientesInMemory = new ArrayList<>();

    protected List<Cliente> carregarDoArquivo() {
        return clientesInMemory;
    }

    protected void salvarNoArquivo() {
        // Não faz nada para evitar escrita em disco durante o teste
    }
}