package main.java.view;

import javax.swing.*;
import java.awt.*;

public class GestorDashboardGUI extends JFrame {
    public GestorDashboardGUI() {
        setTitle("Painel do Gestor");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnProdutos = new JButton("Gerenciar Produtos");
        JButton btnClientes = new JButton("Gerenciar Clientes");

        btnProdutos.addActionListener(e -> new ProdutoGUI().setVisible(true));
        btnClientes.addActionListener(e -> new ClienteGUI().setVisible(true));

        JPanel painel = new JPanel(new GridLayout(0, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.add(btnProdutos);
        painel.add(btnClientes);

        add(painel);
    }
}
