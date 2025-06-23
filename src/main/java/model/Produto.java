package main.java.model;

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int proximoId = 1;

    private int id;
    private String descricao;
    private Categoria categoria;
    private Marca marca;
    private double preco;
    private int qtd;

    public Produto(String descricao, Categoria categoria, Marca marca, double preco, int qtd) {
        this.id = proximoId++;
        this.descricao = descricao;
        this.categoria = categoria;
        this.marca = marca;
        this.preco = preco;
        this.qtd = qtd;
    }

    public Produto(String descricao, String categoria, String marca, double preco, int qtd) {
        this.id = proximoId++;
        this.descricao = descricao;
        this.categoria = new Categoria(categoria);
        this.marca = new Marca(marca);
        this.preco = preco;
        this.qtd = qtd;
    }

    public int getId() { return id; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Categoria getCategoria() { return categoria; }

    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Marca getMarca() { return marca; }

    public void setMarca(Marca marca) { this.marca = marca; }

    public double getPreco() { return preco; }

    public void setPreco(double preco) { this.preco = preco; }

    public int getQtd() { return qtd; }

    public void setQtd(int qtd) { this.qtd = qtd; }

    @Override
    public String toString() {
        return "[" + id + "] - " + descricao + " (" + marca.getNome() + ") - R$" + preco + " [" + qtd + " un.]";
    }
}
