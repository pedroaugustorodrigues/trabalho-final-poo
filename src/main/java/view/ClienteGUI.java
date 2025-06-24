package main.java.view;

import main.java.model.Cliente;
import main.java.repository.ClienteRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tela de gerenciamento de clientes.
 * Permite listar, adicionar, editar e remover clientes.
 *
 * @author Rafael
 */
public class ClienteGUI extends JPanel {
    private ClienteRepository repo = new ClienteRepository();
    private DefaultTableModel tabelaModel;
    private JTable tabelaClientes;
    
    // Cores da identidade visual
    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(108, 99, 255);
    private static final Color ACCENT_COLOR = new Color(32, 201, 151);
    private static final Color DANGER_COLOR = new Color(230, 86, 86);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    @SuppressWarnings("unused")
    private static final Color SECONDARY_TEXT = new Color(120, 120, 120);

    /**
     * Construtor da tela de clientes.
     */
    @SuppressWarnings("unused")
    public ClienteGUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Título
        JLabel titleLabel = new JLabel("Gerenciamento de Clientes");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Painel principal com tabela
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CARD_BG);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Configuração da tabela
        String[] colunas = {"CPF", "Nome", "Email", "Celular"};
        tabelaModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // impede edição direta na tabela
            }
        };

        tabelaClientes = new JTable(tabelaModel);
        
        // Estilizar a tabela
        tabelaClientes.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabelaClientes.setBackground(CARD_BG);
        tabelaClientes.setSelectionBackground(PRIMARY_COLOR);
        tabelaClientes.setSelectionForeground(Color.WHITE);
        tabelaClientes.setGridColor(new Color(220, 220, 220));
        tabelaClientes.setRowHeight(40);
        tabelaClientes.setShowGrid(true);
        tabelaClientes.setIntercellSpacing(new Dimension(1, 1));
        
        tabelaClientes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabelaClientes.getTableHeader().setBackground(PRIMARY_COLOR);
        tabelaClientes.getTableHeader().setForeground(Color.WHITE);
        tabelaClientes.getTableHeader().setBorder(new LineBorder(PRIMARY_COLOR, 1));

        tabelaClientes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(150);  // CPF
        tabelaClientes.getColumnModel().getColumn(1).setPreferredWidth(300);  // Nome
        tabelaClientes.getColumnModel().getColumn(2).setPreferredWidth(250);  // Email
        tabelaClientes.getColumnModel().getColumn(3).setPreferredWidth(150);  // Celular

        tabelaClientes.getTableHeader().setResizingAllowed(false);
        tabelaClientes.getTableHeader().setReorderingAllowed(false);

        tabelaClientes.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = tabelaClientes.rowAtPoint(e.getPoint());
                int column = tabelaClientes.columnAtPoint(e.getPoint());
                if (row > -1) {
                    Object value = tabelaClientes.getValueAt(row, column);
                    if (value != null) {
                        tabelaClientes.setToolTipText(value.toString());
                    }
                } else {
                    tabelaClientes.setToolTipText(null);
                }
            }
        });

        // Centralizar todas as colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Aplicar centralização para todas as colunas
        for (int i = 0; i < tabelaClientes.getColumnCount(); i++) {
            tabelaClientes.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_COLOR;
                this.trackColor = new Color(240, 240, 240);
            }
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel de botões estilizado
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setBackground(BACKGROUND_COLOR);
        painelBotoes.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnAdicionar = createStyledButton("Adicionar Cliente", ACCENT_COLOR, "➕");
        JButton btnEditar = createStyledButton("Editar Cliente", PRIMARY_COLOR, "✏️");
        JButton btnRemover = createStyledButton("Remover Cliente", DANGER_COLOR, "🗑️");

        btnAdicionar.addActionListener(e -> adicionarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnRemover.addActionListener(e -> removerCliente());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);

        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarClientes();
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
     * Carrega os clientes do repositório na tabela.
     */
    private void carregarClientes() {
        tabelaModel.setRowCount(0); // Limpa a tabela
        for (Cliente c : repo.listar()) {
            tabelaModel.addRow(new Object[]{
                c.getCpf(),
                c.getNome(),
                c.getEmail(),
                c.getCelular()
            });
        }
    }

    /**
     * Abre o modal para adicionar um novo cliente.
     */
    private void adicionarCliente() {
        AdicionarClienteModal modal = new AdicionarClienteModal((Frame) SwingUtilities.getWindowAncestor(this));
        modal.setVisible(true);
        if (modal.isClienteAdicionado()) {
            carregarClientes(); // Recarrega a tabela para mostrar o novo cliente
        }
    }

    /**
     * Abre o modal para editar o cliente selecionado.
     */
    private void editarCliente() {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow != -1) {
            String cpf = (String) tabelaModel.getValueAt(selectedRow, 0);
            Cliente cliente = repo.buscarPorCpf(cpf);
            if (cliente != null) {
                EditarClienteModal modal = new EditarClienteModal((Frame) SwingUtilities.getWindowAncestor(this), cliente);
                modal.setVisible(true);
                if (modal.isClienteEditado()) {
                    carregarClientes();
                    JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Remove o cliente selecionado após confirmação.
     */
    private void removerCliente() {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow != -1) {
            String cpf = (String) tabelaModel.getValueAt(selectedRow, 0);
            String nome = (String) tabelaModel.getValueAt(selectedRow, 1);
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja remover o cliente " + nome + "?", 
                "Confirmar Remoção", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                repo.removerPorCpf(cpf);
                tabelaModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
