package main.java.model;

import java.io.Serializable;

/**
 * Representa um item do carrinho de compras.
 */
public class CarrinhoItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Produto produto;
    private int quantidade;

    public CarrinhoItem(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        return produto.getDescricao() + " x " + quantidade + " = R$" + getSubtotal();
    }
}
