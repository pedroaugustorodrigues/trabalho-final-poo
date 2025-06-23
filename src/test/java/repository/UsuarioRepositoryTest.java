package test.java.repository;

import main.java.model.Cliente;
import main.java.model.Usuario;
import org.junit.jupiter.api.*;
import test.java.utils.FieldUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioRepositoryTest {

    @BeforeEach
    void limparArquivo() {
        UsuarioRepositoryEmMemoria.limparArquivoTeste();
        Usuario.resetProximoId();
    }

    @Test
    void testAdicionarEListarUsuario() {
        UsuarioRepositoryEmMemoria repo = new UsuarioRepositoryEmMemoria();
        Usuario u = new Cliente("João", "joao@email.com", "senha123");

        repo.adicionarUsuario(u);
        List<Usuario> usuarios = repo.listarTodos();

        assertEquals(1, usuarios.size());
        assertEquals("João", usuarios.get(0).getNome());
    }

    @Test
    void testBuscarPorId() {
        UsuarioRepositoryEmMemoria repo = new UsuarioRepositoryEmMemoria();
        Usuario u = new Cliente("Maria", "maria@email.com", "1234");
        repo.adicionarUsuario(u);

        Optional<Usuario> encontrado = repo.buscarPorId(u.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Maria", encontrado.get().getNome());
    }

    @Test
    void testBuscarPorEmail() {
        UsuarioRepositoryEmMemoria repo = new UsuarioRepositoryEmMemoria();
        Usuario u = new Cliente("Ana", "ana@email.com", "abc");
        repo.adicionarUsuario(u);

        Optional<Usuario> encontrado = repo.buscarPorEmail("ana@email.com");

        assertTrue(encontrado.isPresent());
        assertEquals("Ana", encontrado.get().getNome());
    }

    @Test
    void testAtualizarUsuario() {
        UsuarioRepositoryEmMemoria repo = new UsuarioRepositoryEmMemoria();
        Usuario u = new Cliente("Pedro", "pedro@email.com", "senha");
        repo.adicionarUsuario(u);

        Usuario atualizado = new Cliente("Pedro Atualizado", "pedro@email.com", "novaSenha");
        FieldUtils.setIdManualmente(atualizado, u.getId());

        repo.atualizarUsuario(atualizado);

        Optional<Usuario> buscado = repo.buscarPorId(u.getId());
        assertTrue(buscado.isPresent());
        assertEquals("Pedro Atualizado", buscado.get().getNome());
    }

    @Test
    void testRemoverUsuario() {
        UsuarioRepositoryEmMemoria repo = new UsuarioRepositoryEmMemoria();
        Usuario u = new Cliente("Lucas", "lucas@email.com", "pass");
        repo.adicionarUsuario(u);

        boolean removido = repo.removerUsuario(u.getId());

        assertTrue(removido);
        assertTrue(repo.listarTodos().isEmpty());
    }
}
