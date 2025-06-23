package test.java.repository;

import main.java.model.Marca;
import main.java.repository.MarcaRepository;

import java.util.ArrayList;
import java.util.List;

public class MarcaRepositoryEmMemoria extends MarcaRepository {
    private List<Marca> marcasInMemory = new ArrayList<>();

    protected List<Marca> carregarDoArquivo() {
        return marcasInMemory;
    }

    protected void salvarNoArquivo() {
        // não salva nada
    }

    // Sobrescreve o construtor para evitar adicionar marcas padrão
    public MarcaRepositoryEmMemoria() {
        // sobrescreve sem chamar adicionarMarcasPadrao()
        this.marcasInMemory = new ArrayList<>();
    }
}
