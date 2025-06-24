package test.java.repository;

import main.java.model.Cliente;
import main.java.model.ItemVenda;
import main.java.model.Produto;
import main.java.model.Categoria;
import main.java.model.Marca;
import main.java.model.Venda;
import main.java.repository.VendaRepository;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class VendaRepositoryTest {

    private static final String ARQUIVO = "vendas.dat";
    private VendaRepository repo;

    @BeforeEach
    void setup() {
        File arquivo = new File(ARQUIVO);
        if (arquivo.exists()) {
            arquivo.delete();
        }
        repo = new VendaRepository();
    }

    private Venda criarVendaExemplo() {
        Cliente cliente = new Cliente("Teste Cliente", "123.456.789-00", "cliente@email.com", "99999-9999", "senha");

        Produto produto = new Produto("Notebook", new Categoria("Informática"), new Marca("Dell"), 3500.0, 5);
        ItemVenda item = new ItemVenda(produto, 2, "M", "Preto");

        List<ItemVenda> itens = new ArrayList<>();
        itens.add(item);

        return new Venda(cliente, itens);
    }

    @Test
    void testAdicionarVenda() {
        Venda venda = criarVendaExemplo();

        repo.adicionar(venda);
        List<Venda> vendas = repo.listar();

        assertEquals(1, vendas.size());
        assertEquals(venda.getId(), vendas.get(0).getId());
        assertEquals(venda.getCliente().getNome(), vendas.get(0).getCliente().getNome());
    }

    @Test
    void testRemoverPorId() {
        Venda venda = criarVendaExemplo();
        repo.adicionar(venda);

        repo.removerPorId(venda.getId());
        List<Venda> vendas = repo.listar();

        assertTrue(vendas.isEmpty());
    }

    @Test
    void testListarVendas() {
        Venda venda1 = criarVendaExemplo();
        Venda venda2 = criarVendaExemplo();

        repo.adicionar(venda1);
        repo.adicionar(venda2);

        List<Venda> vendas = repo.listar();

        assertEquals(2, vendas.size());
        assertTrue(vendas.stream().anyMatch(v -> v.getId() == venda1.getId()));
        assertTrue(vendas.stream().anyMatch(v -> v.getId() == venda2.getId()));
    }

    @Test
    void testBuscarPorId() {
        Venda venda = criarVendaExemplo();
        repo.adicionar(venda);

        Optional<Venda> encontrada = repo.buscarPorId(venda.getId());

        assertTrue(encontrada.isPresent());
        assertEquals(venda.getId(), encontrada.get().getId());
    }
}