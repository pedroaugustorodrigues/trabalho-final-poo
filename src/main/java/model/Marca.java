package main.java.model;

import java.io.Serializable;

/**
 * Representa uma marca de produto.
 */
public class Marca implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int proximoId = 1;
    
    private int id;
    private String nome;
    
    /**
     * Cria uma nova marca.
     */
    public Marca(String nome) {
        this.id = proximoId++;
        this.nome = nome;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }

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
        Marca marca = (Marca) obj;
        return id == marca.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}