package main.java.model;

import java.io.Serializable;

/**
 * Representa uma categoria de produto.
 */
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int proximoId = 1;
    
    private int id;
    private String nome;
    
    /**
     * Cria uma nova categoria.
     */
    public Categoria(String nome) {
        this.id = proximoId++;
        this.nome = nome;
    }
    
    public Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
        if (id >= proximoId) {
            proximoId = id + 1;
        }
    }
    
    public int getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
        return nome;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categoria categoria = (Categoria) obj;
        return id == categoria.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
} 