package main.java.model;

import java.io.Serializable;

public class ItemVenda implements Serializable {
    private static final long serialVersionUID = 1L;

    private Produto produto;
    private int quantidade;
    private double subtotal;
    private String tamanho;
    private String cor;

    public ItemVenda(Produto produto, int quantidade, String tamanho, String cor) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.subtotal = produto.getPreco() * quantidade;
        this.tamanho = tamanho;
        this.cor = cor;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "ItemVenda{" +
                "produto=" + produto.getDescricao() +
                ", quantidade=" + quantidade +
                ", subtotal=" + subtotal +
                ", tamanho='" + tamanho + '\'' +
                ", cor='" + cor + '\'' +
                '}';
    }
} 