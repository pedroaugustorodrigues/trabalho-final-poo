package main.java.view;

import main.java.model.Produto;
import main.java.repository.ProdutoRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProdutoGUI extends JFrame {
    private ProdutoRepository repo = new ProdutoRepository();
    private DefaultListModel<Produto> listModel = new DefaultListModel<>();
    private JList<Produto> listaProdutos = new JList<>(listModel);

    public ProdutoGUI() {
        setTitle("Gerenciar Produtos");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnEditar.addActionListener(e -> editarProduto());
        btnRemover.addActionListener(e -> removerProduto());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);

        add(new JScrollPane(listaProdutos), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void adicionarProduto() {
        Produto p = dialogProduto(null);
        if (p != null) {
            repo.adicionar(p);
            listModel.addElement(p);
        }
    }

    private void editarProduto() {
        Produto selecionado = listaProdutos.getSelectedValue();
        if (selecionado != null) {
            Produto editado = dialogProduto(selecionado);
            if (editado != null) {
                listModel.setElementAt(editado, listaProdutos.getSelectedIndex());
            }
        }
    }

    private void removerProduto() {
        Produto selecionado = listaProdutos.getSelectedValue();
        if (selecionado != null) {
            repo.remover(selecionado);
            listModel.removeElement(selecionado);
        }
    }

    private Produto dialogProduto(Produto original) {
        JTextField desc = new JTextField();
        JTextField cat = new JTextField();
        JTextField marca = new JTextField();
        JTextField preco = new JTextField();
        JTextField qtd = new JTextField();

        if (original != null) {
            desc.setText(original.getDescricao());
            cat.setText(original.getCategoria());
            marca.setText(original.getMarca());
            preco.setText(String.valueOf(original.getPreco()));
            qtd.setText(String.valueOf(original.getQtd()));
        }

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Descrição:")); panel.add(desc);
        panel.add(new JLabel("Categoria:")); panel.add(cat);
        panel.add(new JLabel("Marca:")); panel.add(marca);
        panel.add(new JLabel("Preço:")); panel.add(preco);
        panel.add(new JLabel("Quantidade:")); panel.add(qtd);

        int res = JOptionPane.showConfirmDialog(this, panel, "Detalhes do Produto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                String d = desc.getText();
                String c = cat.getText();
                String m = marca.getText();
                double p = Double.parseDouble(preco.getText());
                int q = Integer.parseInt(qtd.getText());
                if (original != null) {
                    original.setDescricao(d);
                    original.setCategoria(c);
                    original.setMarca(m);
                    original.setPreco(p);
                    original.setQtd(q);
                    return original;
                }
                return new Produto(d, c, m, p, q);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao preencher campos.");
            }
        }
        return null;
    }
}
