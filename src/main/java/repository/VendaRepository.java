package main.java.repository;

import main.java.model.Venda;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VendaRepository {
    private final String arquivo = "vendas.dat";
    private List<Venda> vendas;

    public VendaRepository() {
        vendas = carregarDoArquivo();
        atualizarProximoId();
    }

    private void atualizarProximoId() {
        if (!vendas.isEmpty()) {
            int maxId = vendas.stream().mapToInt(Venda::getId).max().orElse(0);
            Venda.setProximoId(maxId + 1);
        }
    }

    public void adicionar(Venda venda) {
        vendas.add(venda);
        salvarNoArquivo();
    }

    public void removerPorId(int id) {
        vendas.removeIf(v -> v.getId() == id);
        salvarNoArquivo();
    }

    public List<Venda> listar() {
        return new ArrayList<>(vendas);
    }

    public Optional<Venda> buscarPorId(int id) {
        return vendas.stream().filter(v -> v.getId() == id).findFirst();
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(vendas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Venda> carregarDoArquivo() {
        File file = new File(arquivo);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Venda>) obj;
            }
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
} 