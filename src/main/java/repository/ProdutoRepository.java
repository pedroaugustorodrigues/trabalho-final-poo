package main.java.repository;

import main.java.model.Produto;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    private static final String ARQUIVO = "produtos.dat";
    private List<Produto> produtos;

    public ProdutoRepository() {
        produtos = carregarDoArquivo();
    }

    public void adicionar(Produto p) {
        produtos.add(p);
        salvarNoArquivo();
    }

    public void remover(Produto p) {
        produtos.removeIf(produto -> produto.getId() == p.getId());
        salvarNoArquivo();
    }

    public List<Produto> listar() {
        return new ArrayList<>(produtos);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Produto> carregarDoArquivo() {
        File file = new File(ARQUIVO);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Produto> lista = (List<Produto>) ois.readObject();

            // Recupera o maior ID e atualiza o campo estático de Produto
            int maxId = lista.stream().mapToInt(Produto::getId).max().orElse(0);

            Field field = Produto.class.getDeclaredField("proximoId");
            field.setAccessible(true);
            field.setInt(null, maxId + 1);

            return lista;
        } catch (IOException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}