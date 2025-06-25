package main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa o carrinho de compras de um cliente.
 */
public class Carrinho implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<CarrinhoItem> itens;

    public Carrinho() {
        this.itens = new ArrayList<>();
    }

    public List<CarrinhoItem> getItens() {
        return itens;
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        for (CarrinhoItem item : itens) {
            if (item.getProduto().getId() == produto.getId()) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        itens.add(new CarrinhoItem(produto, quantidade));
    }

    public void removerProduto(Produto produto) {
        itens.removeIf(item -> item.getProduto().getId() == produto.getId());
    }

    public void limpar() {
        itens.clear();
    }

    public double getTotal() {
        double soma = 0;
        for (CarrinhoItem item : itens) {
            soma += item.getSubtotal();
        }
        return soma;
    }

    public boolean isVazio() {
        return itens.isEmpty();
    }
}
