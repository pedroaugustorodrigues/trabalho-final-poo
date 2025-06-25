package main.java.repository;

import main.java.model.Categoria;
import main.java.model.Marca;
import main.java.model.Produto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório de produtos.
 * Responsável por persistir, buscar, atualizar e remover produtos do sistema.
 *
 * @author Claudio
 */
public class ProdutoRepository {
    private static final String ARQUIVO = "produtos.dat";
    private List<Produto> produtos;

    /**
     * Construtor padrão. Carrega os produtos do arquivo e adiciona produtos padrão se necessário.
     */
    public ProdutoRepository() {
        produtos = carregarDoArquivo();
        if (produtos.isEmpty()) {
            adicionarProdutosPadrao();
        }
    }

    /**
     * Adiciona um novo produto e salva no arquivo.
     */
    public void adicionar(Produto p) {
        produtos.add(p);
        salvarNoArquivo();
    }

    /**
     * Remove um produto e salva no arquivo.
     */
    public void remover(Produto p) {
        produtos.removeIf(produto -> produto.getId() == p.getId());
        salvarNoArquivo();
    }

    /**
     * Lista todos os produtos.
     */
    public List<Produto> listar() {
        produtos = carregarDoArquivo();
        return new ArrayList<>(produtos);
    }

    /**
     * Atualiza um produto existente e salva no arquivo.
     */
    public void atualizar(Produto produtoAtualizado) {
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getId() == produtoAtualizado.getId()) {
                produtos.set(i, produtoAtualizado);
                salvarNoArquivo();
                return;
            }
        }
    }

    /**
     * Busca um produto pelo ID.
     */
    public java.util.Optional<Produto> buscarPorId(int id) {
        return produtos.stream().filter(p -> p.getId() == id).findFirst();
    }

    /**
     * Lista todas as categorias disponíveis.
     */
    public List<Categoria> listarCategorias() {
        CategoriaRepository categoriaRepo = new CategoriaRepository();
        return categoriaRepo.listar();
    }

    /**
     * Lista todas as marcas disponíveis.
     */
    public List<Marca> listarMarcas() {
        MarcaRepository marcaRepo = new MarcaRepository();
        return marcaRepo.listar();
    }

    /**
     * Salva a lista de produtos no arquivo.
     */
    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de produtos do arquivo.
     */
    @SuppressWarnings("unchecked")
    private List<Produto> carregarDoArquivo() {
        File file = new File(ARQUIVO);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Produto> lista = (List<Produto>) ois.readObject();
            int maxId = lista.stream().mapToInt(Produto::getId).max().orElse(0);
            java.lang.reflect.Field field = Produto.class.getDeclaredField("proximoId");
            field.setAccessible(true);
            field.setInt(null, maxId + 1);
            return lista;
        } catch (IOException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Adiciona produtos padrão ao repositório.
     */
    private void adicionarProdutosPadrao() {
        // Smartphones
        adicionar(new Produto("iPhone 14 Pro", new Categoria("Celulares"), new Marca("Apple"), 8999.99, 15));
        adicionar(new Produto("Samsung Galaxy S23", new Categoria("Celulares"), new Marca("Samsung"), 5999.99, 20));
        adicionar(new Produto("Xiaomi Redmi Note 12", new Categoria("Celulares"), new Marca("Xiaomi"), 1899.99, 25));
        // Notebooks
        adicionar(new Produto("MacBook Pro M2", new Categoria("Notebooks"), new Marca("Apple"), 15999.99, 8));
        adicionar(new Produto("Dell Inspiron 15", new Categoria("Notebooks"), new Marca("Dell"), 4599.99, 12));
        adicionar(new Produto("Lenovo ThinkPad X1", new Categoria("Notebooks"), new Marca("Lenovo"), 8999.99, 10));
        // Tablets
        adicionar(new Produto("iPad Air", new Categoria("Tablets"), new Marca("Apple"), 4999.99, 15));
        adicionar(new Produto("Samsung Galaxy Tab S9", new Categoria("Tablets"), new Marca("Samsung"), 3999.99, 18));
        // Fones de Ouvido
        adicionar(new Produto("AirPods Pro", new Categoria("Fones de Ouvido"), new Marca("Apple"), 2499.99, 30));
        adicionar(new Produto("Sony WH-1000XM5", new Categoria("Fones de Ouvido"), new Marca("Sony"), 2999.99, 12));
        adicionar(new Produto("JBL Tune 510BT", new Categoria("Fones de Ouvido"), new Marca("JBL"), 399.99, 40));
        // Televisões
        adicionar(new Produto("LG OLED C3 55\"", new Categoria("Televisões"), new Marca("LG"), 8999.99, 8));
        adicionar(new Produto("Samsung QLED 65\"", new Categoria("Televisões"), new Marca("Samsung"), 7999.99, 10));
        // Monitores
        adicionar(new Produto("Dell UltraSharp 27\"", new Categoria("Monitores"), new Marca("Dell"), 1899.99, 15));
        adicionar(new Produto("ASUS ProArt 24\"", new Categoria("Monitores"), new Marca("Asus"), 1299.99, 20));
        // Câmeras
        adicionar(new Produto("Sony Alpha A7 IV", new Categoria("Câmeras"), new Marca("Sony"), 15999.99, 5));
        adicionar(new Produto("Canon EOS R6", new Categoria("Câmeras"), new Marca("Canon"), 12999.99, 8));
        // Componentes
        adicionar(new Produto("NVIDIA RTX 4080", new Categoria("Componentes"), new Marca("Nvidia"), 8999.99, 6));
        adicionar(new Produto("Intel Core i9-13900K", new Categoria("Componentes"), new Marca("Intel"), 3999.99, 12));
        // Periféricos
        adicionar(new Produto("Logitech MX Master 3", new Categoria("Periféricos"), new Marca("Logitech"), 599.99, 25));
        adicionar(new Produto("Razer BlackWidow V3", new Categoria("Periféricos"), new Marca("Razer"), 899.99, 18));
        // Acessórios
        adicionar(new Produto("Apple Watch Series 8", new Categoria("Acessórios"), new Marca("Apple"), 3999.99, 20));
        adicionar(new Produto("Samsung Galaxy Watch 6", new Categoria("Acessórios"), new Marca("Samsung"), 2499.99, 15));
    }
}