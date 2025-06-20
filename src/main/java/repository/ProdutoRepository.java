package main.java.repository;

import main.java.model.Produto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    private final String arquivo = "produtos.dat";
    private List<Produto> produtos;

    public ProdutoRepository() {
        produtos = carregarDoArquivo();
    }

    public void adicionar(Produto p) {
        produtos.add(p);
        salvarNoArquivo();
    }

    public void remover(Produto p) {
        produtos.remove(p);
        salvarNoArquivo();
    }

    public List<Produto> listar() {
        return produtos;
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Produto> carregarDoArquivo() {
        File file = new File(arquivo);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Produto>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
