package test.java.repository;

import main.java.model.Produto;
import main.java.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositoryEmMemoria extends ProdutoRepository {
    private final List<Produto> produtosInMemory = new ArrayList<>();

    protected void salvarNoArquivo() {
        // Não salva em disco
    }

    protected List<Produto> carregarDoArquivo() {
        return produtosInMemory;
    }
}
