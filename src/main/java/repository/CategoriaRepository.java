package main.java.repository;

import main.java.model.Categoria;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {
    private final String arquivo = "categorias.dat";
    private List<Categoria> categorias;

    public CategoriaRepository() {
        categorias = carregarDoArquivo();
        if (categorias.isEmpty()) {
            adicionarCategoriasPadrao();
        }
    }

    private void adicionarCategoriasPadrao() {
        adicionar(new Categoria("Celulares"));
        adicionar(new Categoria("Notebooks"));
        adicionar(new Categoria("Tablets"));
        adicionar(new Categoria("Fones de Ouvido"));
        adicionar(new Categoria("Televisões"));
        adicionar(new Categoria("Monitores"));
        adicionar(new Categoria("Câmeras"));
        adicionar(new Categoria("Componentes"));
        adicionar(new Categoria("Periféricos"));
        adicionar(new Categoria("Acessórios"));
    }

    public void adicionar(Categoria categoria) {
        if (!categorias.contains(categoria)) {
            categorias.add(categoria);
            salvarNoArquivo();
        }
    }

    public void atualizar(Categoria categoriaAtualizada) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId() == categoriaAtualizada.getId()) {
                categorias.set(i, categoriaAtualizada);
                salvarNoArquivo();
                return;
            }
        }
    }

    public void remover(int id) {
        categorias.removeIf(c -> c.getId() == id);
        salvarNoArquivo();
    }

    public Categoria buscarPorId(int id) {
        for (Categoria categoria : categorias) {
            if (categoria.getId() == id) {
                return categoria;
            }
        }
        return null;
    }

    public Categoria buscarPorNome(String nome) {
        for (Categoria categoria : categorias) {
            if (categoria.getNome().equalsIgnoreCase(nome)) {
                return categoria;
            }
        }
        return null;
    }

    public List<Categoria> listar() {
        return new ArrayList<>(categorias);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(categorias);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Categoria> carregarDoArquivo() {
        File file = new File(arquivo);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Categoria>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
} 