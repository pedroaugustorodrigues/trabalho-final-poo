package test.java.repository;

import main.java.model.Categoria;
import main.java.model.Marca;
import main.java.model.Produto;
import org.junit.jupiter.api.Test;
import test.java.utils.FieldUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoRepositoryTest {

    @Test
    void testAdicionarProduto() {
        ProdutoRepositoryEmMemoria repo = new ProdutoRepositoryEmMemoria();
        Produto produto = new Produto("Notebook", new Categoria("Informática"), new Marca("Dell"), 2500.0, 10);

        repo.adicionar(produto);

        List<Produto> produtos = repo.listar();
        assertTrue(produtos.contains(produto), "O produto deve ser adicionado ao repositório");
    }

    @Test
    void testRemoverProduto() {
        ProdutoRepositoryEmMemoria repo = new ProdutoRepositoryEmMemoria();
        Produto produto = new Produto("Smartphone", new Categoria("Eletrônicos"), new Marca("Samsung"), 1800.0, 5);

        repo.adicionar(produto);
        repo.remover(produto);

        List<Produto> produtos = repo.listar();
        assertFalse(produtos.contains(produto), "O produto removido não deve estar na lista");
    }

    @Test
    void testAtualizarProduto() {
        ProdutoRepositoryEmMemoria repo = new ProdutoRepositoryEmMemoria();
        Produto produto = new Produto("Monitor", new Categoria("Informática"), new Marca("LG"), 1000.0, 8);
        repo.adicionar(produto);

        Produto atualizado = new Produto("Monitor Gamer", new Categoria("Informática"), new Marca("LG"), 1400.0, 12);

        // Atualiza mantendo o mesmo ID
        FieldUtils.setIdManualmente(atualizado, produto.getId());
        repo.atualizar(atualizado);

        Optional<Produto> buscado = repo.buscarPorId(produto.getId());
        assertTrue(buscado.isPresent(), "O produto atualizado deve ser encontrado");
        assertEquals("Monitor Gamer", buscado.get().getDescricao());
        assertEquals(1400.0, buscado.get().getPreco());
        assertEquals(12, buscado.get().getQtd());
    }

    @Test
    void testBuscarPorId() {
        ProdutoRepositoryEmMemoria repo = new ProdutoRepositoryEmMemoria();
        Produto produto = new Produto("Teclado", new Categoria("Periféricos"), new Marca("Logitech"), 200.0, 20);
        repo.adicionar(produto);

        Optional<Produto> encontrado = repo.buscarPorId(produto.getId());

        assertTrue(encontrado.isPresent(), "Produto deve ser encontrado");
        assertEquals("Teclado", encontrado.get().getDescricao());
    }

    @Test
    void testListarProdutos() {
        ProdutoRepositoryEmMemoria repo = new ProdutoRepositoryEmMemoria();
        Produto p1 = new Produto("Câmera", new Categoria("Foto"), new Marca("Sony"), 1200.0, 4);
        Produto p2 = new Produto("Fone", new Categoria("Áudio"), new Marca("JBL"), 300.0, 15);

        repo.adicionar(p1);
        repo.adicionar(p2);

        List<Produto> produtos = repo.listar();

        assertEquals(2, produtos.size(), "Deve haver dois produtos na lista");
        assertTrue(produtos.contains(p1));
        assertTrue(produtos.contains(p2));
    }
}