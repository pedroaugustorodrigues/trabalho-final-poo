package main.java.repository;

import main.java.model.Categoria;
import main.java.model.Marca;
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

    public void atualizar(Produto produtoAtualizado) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == produtoAtualizado.getId()) {
                produtos.set(i, produtoAtualizado);
                salvarNoArquivo();
                return;
            }
        }
    }

    public java.util.Optional<Produto> buscarPorId(int id) {
        return produtos.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Categoria> listarCategorias() {
        CategoriaRepository categoriaRepo = new CategoriaRepository();
        return categoriaRepo.listar();
    }

    public List<Marca> listarMarcas() {
        MarcaRepository marcaRepo = new MarcaRepository();
        return marcaRepo.listar();
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Produto> carregarDoArquivo() {
        File file = new File(ARQUIVO);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Produto> lista = (List<Produto>) ois.readObject();

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