package main.java.model;

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String descricao;
    private String categoria;
    private String marca;
    private double preco;
    private int qtd;

    public Produto(String descricao, String categoria, String marca, double preco, int qtd) {
        this.descricao = descricao;
        this.categoria = categoria;
        this.marca = marca;
        this.preco = preco;
        this.qtd = qtd;
    }

    // Getters e Setters
    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getMarca() { return marca; }

    public void setMarca(String marca) { this.marca = marca; }

    public double getPreco() { return preco; }

    public void setPreco(double preco) { this.preco = preco; }

    public int getQtd() { return qtd; }

    public void setQtd(int qtd) { this.qtd = qtd; }

    @Override
    public String toString() {
        return descricao + " (" + marca + ") - R$" + preco + " [" + qtd + " un.]";
    }
}
