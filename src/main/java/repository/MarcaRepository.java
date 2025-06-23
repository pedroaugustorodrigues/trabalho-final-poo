package main.java.repository;

import main.java.model.Marca;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MarcaRepository {
    private final String arquivo = "marcas.dat";
    private List<Marca> marcas;

    public MarcaRepository() {
        marcas = carregarDoArquivo();
        if (marcas.isEmpty()) {
            adicionarMarcasPadrao();
        }
    }

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

    public void adicionar(Marca marca) {
        if (!marcas.contains(marca)) {
            marcas.add(marca);
            salvarNoArquivo();
        }
    }

    public void atualizar(Marca marcaAtualizada) {
        for (int i = 0; i < marcas.size(); i++) {
            if (marcas.get(i).getId() == marcaAtualizada.getId()) {
                marcas.set(i, marcaAtualizada);
                salvarNoArquivo();
                return;
            }
        }
    }

    public void remover(int id) {
        marcas.removeIf(m -> m.getId() == id);
        salvarNoArquivo();
    }

    public Marca buscarPorId(int id) {
        for (Marca marca : marcas) {
            if (marca.getId() == id) {
                return marca;
            }
        }
        return null;
    }

    public Marca buscarPorNome(String nome) {
        for (Marca marca : marcas) {
            if (marca.getNome().equalsIgnoreCase(nome)) {
                return marca;
            }
        }
        return null;
    }

    public List<Marca> listar() {
        return new ArrayList<>(marcas);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(marcas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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