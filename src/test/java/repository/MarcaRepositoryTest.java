package test.java.repository;

import main.java.model.Marca;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MarcaRepositoryTest {

    @Test
    void testAdicionarEListarMarca() {
        MarcaRepositoryEmMemoria repo = new MarcaRepositoryEmMemoria();

        Marca marca = new Marca("TesteMarca");

        repo.adicionar(marca);
        List<Marca> marcas = repo.listar();

        assertTrue(marcas.contains(marca), "A lista deve conter a marca adicionada");
    }

    @Test
    void testBuscarPorId() {
        MarcaRepositoryEmMemoria repo = new MarcaRepositoryEmMemoria();

        Marca marca = new Marca("MarcaID");
        repo.adicionar(marca);

        Marca encontrada = repo.buscarPorId(marca.getId());
        assertNotNull(encontrada);
        assertEquals("MarcaID", encontrada.getNome());
    }

    @Test
    void testBuscarPorNome() {
        MarcaRepositoryEmMemoria repo = new MarcaRepositoryEmMemoria();

        Marca marca = new Marca("MarcaNome");
        repo.adicionar(marca);

        Marca encontrada = repo.buscarPorNome("MarcaNome");
        assertNotNull(encontrada);
        assertEquals(marca.getId(), encontrada.getId());
    }

    @Test
    void testRemoverMarca() {
        MarcaRepositoryEmMemoria repo = new MarcaRepositoryEmMemoria();

        Marca marca = new Marca("MarcaRemove");
        repo.adicionar(marca);

        repo.remover(marca.getId());

        assertNull(repo.buscarPorId(marca.getId()));
    }

    @Test
    void testAtualizarMarca() {
        MarcaRepositoryEmMemoria repo = new MarcaRepositoryEmMemoria();

        Marca marca = new Marca("MarcaOriginal");
        repo.adicionar(marca);

        Marca marcaAtualizada = new Marca("MarcaAtualizada");
        // mantém o mesmo ID para atualização
        marcaAtualizada.setId(marca.getId());

        repo.atualizar(marcaAtualizada);

        Marca buscada = repo.buscarPorId(marca.getId());
        assertEquals("MarcaAtualizada", buscada.getNome());
    }
}