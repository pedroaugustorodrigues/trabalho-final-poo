package main.java.model;

import java.io.Serializable;

public class Marca implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int proximoId = 1;
    
    private int id;
    private String nome;
    
    public Marca(String nome) {
        this.id = proximoId++;
        this.nome = nome;
    }
    
    public Marca(int id, String nome) {
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
        Marca marca = (Marca) obj;
        return id == marca.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
} 