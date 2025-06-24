package main.java.view;

import main.java.model.Venda;
import main.java.repository.VendaRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela de gerenciamento de vendas.
 * Permite listar, adicionar e remover vendas.
 *
 * @author Pedro
 */
public class VendasGUI extends JPanel {

    private VendaRepository vendaRepository = new VendaRepository();
    private DefaultTableModel tableModel;
    private JTable table;
    
    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(108, 99, 255);
    private static final Color ACCENT_COLOR = new Color(32, 201, 151);
    private static final Color DANGER_COLOR = new Color(230, 86, 86);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    @SuppressWarnings("unused")
    public VendasGUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Gerenciamento de Vendas");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CARD_BG);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        String[] colunas = {"Cód. da Venda", "Total", "Data", "Nome Cliente", "CPF Cliente"};
        tableModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setBackground(CARD_BG);
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBorder(new LineBorder(PRIMARY_COLOR, 1));
        table.getTableHeader().setReorderingAllowed(false);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);

        table.getTableHeader().setResizingAllowed(false);

        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                if (row > -1) {
                    Object value = table.getValueAt(row, column);
                    if (value != null) {
                        table.setToolTipText(value.toString());
                    }
                } else {
                    table.setToolTipText(null);
                }
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
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

        JButton btnAdicionar = createStyledButton("Nova Venda", ACCENT_COLOR, "➕");
        JButton btnExcluir = createStyledButton("Excluir Venda", DANGER_COLOR, "🗑️");

        btnAdicionar.addActionListener(e -> addVendaAction());
        btnExcluir.addActionListener(e -> deleteVendaAction());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnExcluir);

        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        loadVendas();
    }

    /**
     * Cria um botão estilizado para ações.
     */
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

    /**
     * Carrega as vendas do repositório na tabela.
     */
    private void loadVendas() {
        tableModel.setRowCount(0);
        List<Venda> vendas = vendaRepository.listar();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        for (Venda venda : vendas) {
            String nomeCliente = "N/A";
            String cpfCliente = "N/A";
            if(venda.getCliente() != null) {
                nomeCliente = venda.getCliente().getNome();
                cpfCliente = venda.getCliente().getCpf();
            }

            tableModel.addRow(new Object[]{
                    venda.getId(),
                    String.format("R$ %.2f", venda.getTotal()),
                    venda.getData().format(formatter),
                    nomeCliente,
                    cpfCliente
            });
        }
    }

    /**
     * Abre o modal para adicionar uma nova venda.
     */
    private void addVendaAction() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        NovaVendaGUI novaVendaGUI = new NovaVendaGUI((Frame)owner);
        novaVendaGUI.setVisible(true);
        loadVendas();
    }

    /**
     * Remove a venda selecionada após confirmação.
     */
    private void deleteVendaAction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int vendaId = (int) tableModel.getValueAt(selectedRow, 0);
            String nomeCliente = (String) tableModel.getValueAt(selectedRow, 3);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a venda " + vendaId + " do cliente " + nomeCliente + "?",
                    "Confirmar Exclusão", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                vendaRepository.removerPorId(vendaId);
                loadVendas();
                JOptionPane.showMessageDialog(this, "Venda removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Por favor, selecione uma venda para excluir.",
                    "Nenhuma Venda Selecionada", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }
} 