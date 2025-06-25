package main.java.repository;

import main.java.model.Categoria;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório de categorias.
 * Responsável por persistir, buscar, atualizar e remover categorias do sistema.
 *
 * @author Pedro
 */
public class CategoriaRepository {
    private final String arquivo = "categorias.dat";
    private final List<Categoria> categorias;

    /**
     * Construtor padrão. Carrega as categorias do arquivo e adiciona categorias padrão se necessário.
     */
    public CategoriaRepository() {
        categorias = carregarDoArquivo();
        if (categorias.isEmpty()) {
            adicionarCategoriasPadrao();
        }
    }

    /**
     * Adiciona categorias padrão ao repositório.
     */
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

    /**
     * Adiciona uma nova categoria e salva no arquivo.
     */
    public void adicionar(Categoria categoria) {
        if (!categorias.contains(categoria)) {
            categorias.add(categoria);
            salvarNoArquivo();
        }
    }

    /**
     * Atualiza uma categoria existente e salva no arquivo.
     */
    public void atualizar(Categoria categoriaAtualizada) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId() == categoriaAtualizada.getId()) {
                categorias.set(i, categoriaAtualizada);
                salvarNoArquivo();
                return;
            }
        }
    }

    /**
     * Remove uma categoria pelo ID e salva no arquivo.
     */
    public void remover(int id) {
        categorias.removeIf(c -> c.getId() == id);
        salvarNoArquivo();
    }

    /**
     * Busca uma categoria pelo ID.
     */
    public Categoria buscarPorId(int id) {
        for (Categoria categoria : categorias) {
            if (categoria.getId() == id) {
                return categoria;
            }
        }
        return null;
    }

    /**
     * Busca uma categoria pelo nome.
     */
    public Categoria buscarPorNome(String nome) {
        for (Categoria categoria : categorias) {
            if (categoria.getNome().equalsIgnoreCase(nome)) {
                return categoria;
            }
        }
        return null;
    }

    /**
     * Lista todas as categorias.
     */
    public List<Categoria> listar() {
        // Recarrega os dados do arquivo para garantir que está atualizado
        List<Categoria> categoriasAtualizadas = carregarDoArquivo();
        categorias.clear();
        categorias.addAll(categoriasAtualizadas);
        return new ArrayList<>(categorias);
    }

    /**
     * Salva a lista de categorias no arquivo.
     */
    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(categorias);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a lista de categorias do arquivo.
     */
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