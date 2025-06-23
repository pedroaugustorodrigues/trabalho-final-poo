package main.java.view;

import main.java.model.Produto;
import main.java.repository.ProdutoRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;

public class ProdutoGUI extends JPanel {
    private ProdutoRepository repo = new ProdutoRepository();
    private DefaultTableModel tabelaModel;
    private JTable tabelaProdutos;
    
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private final Color CARD_BG = Color.WHITE;
    private final Color PRIMARY_COLOR = new Color(108, 99, 255);
    private final Color ACCENT_COLOR = new Color(32, 201, 151);
    private final Color DANGER_COLOR = new Color(230, 86, 86);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color SECONDARY_TEXT = new Color(120, 120, 120);

    @SuppressWarnings("unused")
    public ProdutoGUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Gerenciamento de Produtos");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CARD_BG);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        String[] colunas = {"ID", "Descrição", "Categoria", "Marca", "Preço", "Qtd"};
        tabelaModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaProdutos = new JTable(tabelaModel);
        
        tabelaProdutos.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabelaProdutos.setBackground(CARD_BG);
        tabelaProdutos.setSelectionBackground(PRIMARY_COLOR);
        tabelaProdutos.setSelectionForeground(Color.WHITE);
        tabelaProdutos.setGridColor(new Color(220, 220, 220));
        tabelaProdutos.setRowHeight(40);
        tabelaProdutos.setShowGrid(true);
        tabelaProdutos.setIntercellSpacing(new Dimension(1, 1));
        
        tabelaProdutos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabelaProdutos.getTableHeader().setBackground(PRIMARY_COLOR);
        tabelaProdutos.getTableHeader().setForeground(Color.WHITE);
        tabelaProdutos.getTableHeader().setBorder(new LineBorder(PRIMARY_COLOR, 1));

        tabelaProdutos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(80);  
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(300);  
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(150);  
        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(150); 
        tabelaProdutos.getColumnModel().getColumn(4).setPreferredWidth(120);  
        tabelaProdutos.getColumnModel().getColumn(5).setPreferredWidth(100);  

        tabelaProdutos.getTableHeader().setResizingAllowed(false);
        tabelaProdutos.getTableHeader().setReorderingAllowed(false);
        tabelaProdutos.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = tabelaProdutos.rowAtPoint(e.getPoint());
                int column = tabelaProdutos.columnAtPoint(e.getPoint());
                if (row > -1 && column == 1) { 
                    Object value = tabelaProdutos.getValueAt(row, column);
                    if (value != null) {
                        tabelaProdutos.setToolTipText(value.toString());
                    }
                } else {
                    tabelaProdutos.setToolTipText(null);
                }
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        for (int i = 0; i < tabelaProdutos.getColumnCount(); i++) {
            tabelaProdutos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_COLOR;
                this.trackColor = new Color(240, 240, 240);
            }
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setBackground(BACKGROUND_COLOR);
        painelBotoes.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnAdicionar = createStyledButton("Adicionar Produto", ACCENT_COLOR, "➕");
        JButton btnEditar = createStyledButton("Editar Produto", PRIMARY_COLOR, "✏️");
        JButton btnRemover = createStyledButton("Remover Produto", DANGER_COLOR, "🗑️");

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnEditar.addActionListener(e -> editarProduto());
        btnRemover.addActionListener(e -> removerProduto());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);

        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarProdutosNaTabela();
    }

    private JButton createStyledButton(String text, Color bgColor, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void carregarProdutosNaTabela() {
        tabelaModel.setRowCount(0);
        for (Produto p : repo.listar()) {
            tabelaModel.addRow(new Object[]{
                    p.getId(),
                    p.getDescricao(),
                    p.getCategoria().getNome(),
                    p.getMarca().getNome(),
                    String.format("R$ %.2f", p.getPreco()),
                    p.getQtd()
            });
        }
    }

    private void adicionarProduto() {
        AdicionarProdutoModal modal = new AdicionarProdutoModal((Frame) SwingUtilities.getWindowAncestor(this));
        modal.setVisible(true);
        
        if (modal.isProdutoAdicionado()) {
            carregarProdutosNaTabela();
        }
    }

    private void editarProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tabelaModel.getValueAt(selectedRow, 0);
            Optional<Produto> opt = repo.buscarPorId(id);
            if (opt.isPresent()) {
                Produto produto = opt.get();
                EditarProdutoModal modal = new EditarProdutoModal((Frame) SwingUtilities.getWindowAncestor(this), produto);
                modal.setVisible(true);
                
                if (modal.isProdutoEditado()) {
                    carregarProdutosNaTabela();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removerProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tabelaModel.getValueAt(selectedRow, 0);
            Optional<Produto> opt = repo.buscarPorId(id);
            if (opt.isPresent()) {
                Produto produto = opt.get();
                int confirmacao = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja remover o produto " + produto.getDescricao() + "?", 
                    "Confirmar Remoção", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    repo.remover(produto);
                    tabelaModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Produto removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
