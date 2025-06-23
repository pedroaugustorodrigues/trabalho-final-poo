package test.java.repository;

import main.java.model.Cliente;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteRepositoryTest {

    @Test
    void testAdicionarEListarCliente() {
        ClienteRepositoryEmMemoria repo = new ClienteRepositoryEmMemoria();

        Cliente cliente = new Cliente("João", "123.456.789-00", "joao@email.com", "99999-9999", "senha123");

        repo.adicionar(cliente);
        List<Cliente> clientes = repo.listar();

        assertTrue(clientes.contains(cliente), "Deveria conter o cliente adicionado");
    }

    @Test
    void testBuscarPorCpf() {
        ClienteRepositoryEmMemoria repo = new ClienteRepositoryEmMemoria();

        Cliente cliente = new Cliente("Maria", "111.222.333-44", "maria@email.com", "98888-8888", "senha456");

        repo.adicionar(cliente);

        Cliente encontrado = repo.buscarPorCpf("111.222.333-44");
        assertNotNull(encontrado);
        assertEquals("Maria", encontrado.getNome());
    }

    @Test
    void testRemoverPorCpf() {
        ClienteRepositoryEmMemoria repo = new ClienteRepositoryEmMemoria();

        Cliente cliente = new Cliente("Carlos", "555.666.777-88", "carlos@email.com", "97777-7777", "senha789");
        repo.adicionar(cliente);

        repo.removerPorCpf("555.666.777-88");
        assertNull(repo.buscarPorCpf("555.666.777-88"));
    }

    @Test
    void testAtualizarCliente() {
        ClienteRepositoryEmMemoria repo = new ClienteRepositoryEmMemoria();

        Cliente cliente = new Cliente("Ana", "999.888.777-66", "ana@email.com", "96666-6666", "senha000");
        repo.adicionar(cliente);

        // Atualizar email e celular
        Cliente clienteAtualizado = new Cliente("Ana", "999.888.777-66", "ana.novo@email.com", "95555-5555", "senha000");
        repo.atualizar(clienteAtualizado);

        Cliente buscado = repo.buscarPorCpf("999.888.777-66");
        assertNotNull(buscado);
        assertEquals("ana.novo@email.com", buscado.getEmail());
        assertEquals("95555-5555", buscado.getCelular());
    }
}