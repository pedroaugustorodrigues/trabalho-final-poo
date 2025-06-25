package main.java.model;

import java.io.Serializable;

/**
 * Representa um produto do sistema.
 */
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int proximoId = 1;
    private int id;
    private String descricao;
    private Categoria categoria;
    private Marca marca;
    private double preco;
    private int qtd;
    private String caminhoImagem;

    /**
     * Cria um produto com todos os dados obrigatórios.
     */
    public Produto(String descricao, Categoria categoria, Marca marca, double preco, int qtd, String caminhoImagem) {
        this.id = proximoId++;
        this.descricao = descricao;
        this.categoria = categoria;
        this.marca = marca;
        this.preco = preco;
        this.qtd = qtd;
        this.caminhoImagem = caminhoImagem;
    }

    public Produto(String descricao, Categoria categoria, Marca marca, double preco, int qtd) {
        this(descricao, categoria, marca, preco, qtd, null);
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
    public String getCaminhoImagem() { return caminhoImagem; }
    public void setCaminhoImagem(String caminhoImagem) { this.caminhoImagem = caminhoImagem; }

    @Override
    public String toString() {
        return "[" + id + "] - " + descricao + " (" + marca.getNome() + ") - R$" + preco + " [" + qtd + " un.]";
    }
}
