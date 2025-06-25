package main.java.repository;

import main.java.model.Venda;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de vendas.
 * Responsável por persistir, buscar, atualizar e remover vendas do sistema.
 *
 * @author Pedro
 */
public class VendaRepository {
    private final String arquivo = "vendas.dat";
    private final List<Venda> vendas;

    /**
     * Construtor padrão. Carrega as vendas do arquivo e atualiza o próximo ID.
     */
    public VendaRepository() {
        vendas = carregarDoArquivo();
        atualizarProximoId();
    }

    /**
     * Atualiza o próximo ID de venda com base nas vendas carregadas.
     */
    private void atualizarProximoId() {
        if (!vendas.isEmpty()) {
            int maxId = vendas.stream().mapToInt(Venda::getId).max().orElse(0);
            Venda.setProximoId(maxId + 1);
        }
    }

    /**
     * Adiciona uma nova venda e salva no arquivo.
     */
    public void adicionar(Venda venda) {
        vendas.add(venda);
        salvarNoArquivo();
    }

    /**
     * Remove uma venda pelo ID e salva no arquivo.
     */
    public void removerPorId(int id) {
        vendas.removeIf(v -> v.getId() == id);
        salvarNoArquivo();
    }

    /**
     * Lista todas as vendas.
     */
    public List<Venda> listar() {
        // Recarrega os dados do arquivo para garantir que está atualizado
        List<Venda> vendasAtualizadas = carregarDoArquivo();
        vendas.clear();
        vendas.addAll(vendasAtualizadas);
        return new ArrayList<>(vendas);
    }

    /**
     * Busca uma venda pelo ID.
     */
    public Optional<Venda> buscarPorId(int id) {
        return vendas.stream().filter(v -> v.getId() == id).findFirst();
    }

    /**
     * Lista todas as vendas de um cliente.
     */
    public List<Venda> listarPorCliente(main.java.model.Cliente cliente) {
        return vendas.stream()
                .filter(v -> v.getCliente() != null && v.getCliente().getId() == cliente.getId())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Salva a lista de vendas no arquivo.
     */
    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(vendas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de vendas do arquivo.
     */
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