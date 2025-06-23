package main.java.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int proximoId = 1;

    private int id;
    private Cliente cliente;
    private List<ItemVenda> itens;
    private double total;
    private LocalDateTime data;

    public Venda(Cliente cliente, List<ItemVenda> itens) {
        this.id = proximoId++;
        this.cliente = cliente;
        this.itens = itens;
        this.data = LocalDateTime.now();
        this.total = calcularTotal();
    }

    private double calcularTotal() {
        return itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();
    }
    
    public static void setProximoId(int id) {
        proximoId = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
} 