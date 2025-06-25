package main.java.repository;

import main.java.model.Marca;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório de marcas.
 * Responsável por persistir, buscar, atualizar e remover marcas do sistema.
 *
 * @author Pedro
 */
public class MarcaRepository {
    private final String arquivo = "marcas.dat";
    private final List<Marca> marcas;

    /**
     * Construtor padrão. Carrega as marcas do arquivo e adiciona marcas padrão se necessário.
     */
    public MarcaRepository() {
        marcas = carregarDoArquivo();
        if (marcas.isEmpty()) {
            adicionarMarcasPadrao();
        }
    }

    /**
     * Adiciona marcas padrão ao repositório.
     */
    private void adicionarMarcasPadrao() {
        adicionar(new Marca("Apple"));
        adicionar(new Marca("Samsung"));
        adicionar(new Marca("Dell"));
        adicionar(new Marca("HP"));
        adicionar(new Marca("Sony"));
        adicionar(new Marca("LG"));
        adicionar(new Marca("Lenovo"));
        adicionar(new Marca("Asus"));
        adicionar(new Marca("Nvidia"));
        adicionar(new Marca("Intel"));
        adicionar(new Marca("Xiaomi"));
        adicionar(new Marca("JBL"));
        adicionar(new Marca("Logitech"));
    }

    /**
     * Adiciona uma nova marca e salva no arquivo.
     */
    public void adicionar(Marca marca) {
        if (!marcas.contains(marca)) {
            marcas.add(marca);
            salvarNoArquivo();
        }
    }

    /**
     * Atualiza uma marca existente e salva no arquivo.
     */
    public void atualizar(Marca marcaAtualizada) {
        for (int i = 0; i < marcas.size(); i++) {
            if (marcas.get(i).getId() == marcaAtualizada.getId()) {
                marcas.set(i, marcaAtualizada);
                salvarNoArquivo();
                return;
            }
        }
    }

    /**
     * Remove uma marca pelo ID e salva no arquivo.
     */
    public void remover(int id) {
        marcas.removeIf(m -> m.getId() == id);
        salvarNoArquivo();
    }

    /**
     * Busca uma marca pelo ID.
     */
    public Marca buscarPorId(int id) {
        for (Marca marca : marcas) {
            if (marca.getId() == id) {
                return marca;
            }
        }
        return null;
    }

    /**
     * Busca uma marca pelo nome.
     */
    public Marca buscarPorNome(String nome) {
        for (Marca marca : marcas) {
            if (marca.getNome().equalsIgnoreCase(nome)) {
                return marca;
            }
        }
        return null;
    }

    /**
     * Lista todas as marcas.
     */
    public List<Marca> listar() {
        // Recarrega os dados do arquivo para garantir que está atualizado
        List<Marca> marcasAtualizadas = carregarDoArquivo();
        marcas.clear();
        marcas.addAll(marcasAtualizadas);
        return new ArrayList<>(marcas);
    }

    /**
     * Salva a lista de marcas no arquivo.
     */
    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(marcas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de marcas do arquivo.
     */
    @SuppressWarnings("unchecked")
    private List<Marca> carregarDoArquivo() {
        File file = new File(arquivo);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Marca>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
} 