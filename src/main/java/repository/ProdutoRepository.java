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
        if (produtos.isEmpty()) {
            adicionarProdutosPadrao();
        }
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
        produtos = carregarDoArquivo();
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

    private void adicionarProdutosPadrao() {
        // Smartphones
        adicionar(new Produto("iPhone 14 Pro", "Celulares", "Apple", 8999.99, 15));
        adicionar(new Produto("Samsung Galaxy S23", "Celulares", "Samsung", 5999.99, 20));
        adicionar(new Produto("Xiaomi Redmi Note 12", "Celulares", "Xiaomi", 1899.99, 25));
        
        // Notebooks
        adicionar(new Produto("MacBook Pro M2", "Notebooks", "Apple", 15999.99, 8));
        adicionar(new Produto("Dell Inspiron 15", "Notebooks", "Dell", 4599.99, 12));
        adicionar(new Produto("Lenovo ThinkPad X1", "Notebooks", "Lenovo", 8999.99, 10));
        
        // Tablets
        adicionar(new Produto("iPad Air", "Tablets", "Apple", 4999.99, 15));
        adicionar(new Produto("Samsung Galaxy Tab S9", "Tablets", "Samsung", 3999.99, 18));
        
        // Fones de Ouvido
        adicionar(new Produto("AirPods Pro", "Fones de Ouvido", "Apple", 2499.99, 30));
        adicionar(new Produto("Sony WH-1000XM5", "Fones de Ouvido", "Sony", 2999.99, 12));
        adicionar(new Produto("JBL Tune 510BT", "Fones de Ouvido", "JBL", 399.99, 40));
        
        // Televisões
        adicionar(new Produto("LG OLED C3 55\"", "Televisões", "LG", 8999.99, 8));
        adicionar(new Produto("Samsung QLED 65\"", "Televisões", "Samsung", 7999.99, 10));
        
        // Monitores
        adicionar(new Produto("Dell UltraSharp 27\"", "Monitores", "Dell", 1899.99, 15));
        adicionar(new Produto("ASUS ProArt 24\"", "Monitores", "Asus", 1299.99, 20));
        
        // Câmeras
        adicionar(new Produto("Sony Alpha A7 IV", "Câmeras", "Sony", 15999.99, 5));
        adicionar(new Produto("Canon EOS R6", "Câmeras", "Canon", 12999.99, 8));
        
        // Componentes
        adicionar(new Produto("NVIDIA RTX 4080", "Componentes", "Nvidia", 8999.99, 6));
        adicionar(new Produto("Intel Core i9-13900K", "Componentes", "Intel", 3999.99, 12));
        
        // Periféricos
        adicionar(new Produto("Logitech MX Master 3", "Periféricos", "Logitech", 599.99, 25));
        adicionar(new Produto("Razer BlackWidow V3", "Periféricos", "Razer", 899.99, 18));
        
        // Acessórios
        adicionar(new Produto("Apple Watch Series 8", "Acessórios", "Apple", 3999.99, 20));
        adicionar(new Produto("Samsung Galaxy Watch 6", "Acessórios", "Samsung", 2499.99, 15));
        
    }
}