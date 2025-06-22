package main.java.view;

import main.java.model.Produto;
import main.java.repository.ProdutoRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;

public class ProdutoGUI extends JFrame {
    private ProdutoRepository repo = new ProdutoRepository();
    private DefaultTableModel tabelaModel;
    private JTable tabelaProdutos;

    public ProdutoGUI() {
        setTitle("Gerenciamento de Produtos");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        String[] colunas = {"ID", "Descrição", "Categoria", "Marca", "Preço", "Qtd"};
        tabelaModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // impede edição direta na tabela
            }
        };

        tabelaProdutos = new JTable(tabelaModel);

        tabelaProdutos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // desativa redimensionamento automático

        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(220); // Descrição
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(130); // Categoria
        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(130); // Marca
        tabelaProdutos.getColumnModel().getColumn(4).setPreferredWidth(80);  // Preço
        tabelaProdutos.getColumnModel().getColumn(5).setPreferredWidth(70);  // Qtd

        // Impede redimensionamento das colunas
        tabelaProdutos.getTableHeader().setResizingAllowed(false);
        // Impede reordenação das colunas (arrastar cabeçalho)
        tabelaProdutos.getTableHeader().setReorderingAllowed(false);

        tabelaProdutos.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = tabelaProdutos.rowAtPoint(e.getPoint());
                int column = tabelaProdutos.columnAtPoint(e.getPoint());
                if (row > -1 && column == 1) { // coluna 1 = Descrição
                    Object value = tabelaProdutos.getValueAt(row, column);
                    if (value != null) {
                        tabelaProdutos.setToolTipText(value.toString());
                    }
                } else {
                    tabelaProdutos.setToolTipText(null);
                }
            }
        });

        // Centraliza colunas ID e Qtd
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabelaProdutos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        tabelaProdutos.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Qtd

        carregarProdutosNaTabela();

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

        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarProdutosNaTabela() {
        for (Produto p : repo.listar()) {
            tabelaModel.addRow(new Object[]{
                    p.getId(),
                    p.getDescricao(),
                    p.getCategoria(),
                    p.getMarca(),
                    String.format("R$ %.2f", p.getPreco()),
                    p.getQtd()
            });
        }
    }

    private void adicionarProduto() {
        Produto p = dialogProduto(null);
        if (p != null) {
            repo.adicionar(p);
            tabelaModel.addRow(new Object[]{
                    p.getId(),
                    p.getDescricao(),
                    p.getCategoria(),
                    p.getMarca(),
                    String.format("R$ %.2f", p.getPreco()),
                    p.getQtd()
            });
        }
    }

    private void editarProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tabelaModel.getValueAt(selectedRow, 0);
            Optional<Produto> opt = repo.listar().stream().filter(p -> p.getId() == id).findFirst();
            if (opt.isPresent()) {
                Produto original = opt.get();
                Produto editado = dialogProduto(original);
                if (editado != null) {
                    tabelaModel.setValueAt(editado.getDescricao(), selectedRow, 1);
                    tabelaModel.setValueAt(editado.getCategoria(), selectedRow, 2);
                    tabelaModel.setValueAt(editado.getMarca(), selectedRow, 3);
                    tabelaModel.setValueAt(String.format("R$ %.2f", editado.getPreco()), selectedRow, 4);
                    tabelaModel.setValueAt(editado.getQtd(), selectedRow, 5);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
        }
    }

    private void removerProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tabelaModel.getValueAt(selectedRow, 0);
            Optional<Produto> opt = repo.listar().stream().filter(p -> p.getId() == id).findFirst();
            if (opt.isPresent()) {
                repo.remover(opt.get());
                tabelaModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.");
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
