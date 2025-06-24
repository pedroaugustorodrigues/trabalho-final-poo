package main.java.view;

import main.java.model.Cliente;
import main.java.repository.ClienteRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Modal para cadastro de um novo cliente.
 * Exibe formulário para preenchimento dos dados obrigatórios e salva no repositório.
 *
 * @author Rafael
 */
public class AdicionarClienteModal extends JDialog {
    // Cores da identidade visual
    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(32, 201, 151);
    private static final Color DANGER_COLOR = new Color(230, 86, 86);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);

    private final JTextField campoNome;
    private final JTextField campoCpf;
    private final JTextField campoEmail;
    private final JTextField campoCelular;
    private final ClienteRepository repo = new ClienteRepository();
    private boolean clienteAdicionado = false;

    /**
     * Cria o modal de cadastro de cliente.
     * @param parent janela pai
     */
    @SuppressWarnings("unused")
    public AdicionarClienteModal(Frame parent) {
        super(parent, "Novo Cliente", true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CARD_BG);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Título
        JLabel titleLabel = new JLabel("Novo Cliente");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Campos
        campoNome = createStyledTextField("", "Nome Completo");
        campoCpf = createStyledTextField("", "CPF (somente números)");
        campoEmail = createStyledTextField("", "E-mail");
        campoCelular = createStyledTextField("", "Celular (com DDD)");

        mainPanel.add(campoNome);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(campoCpf);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(campoEmail);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(campoCelular);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(CARD_BG);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        JButton btnSalvar = createStyledButton("Salvar", ACCENT_COLOR);
        JButton btnCancelar = createStyledButton("Cancelar", DANGER_COLOR);

        btnSalvar.addActionListener(e -> salvarCliente());
        btnCancelar.addActionListener(e -> dispose());

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 500);
        setLocationRelativeTo(parent);
    }

    /**
     * Salva o cliente no repositório após validação dos campos.
     */
    private void salvarCliente() {
        if (!validarCampos()) {
            return;
        }
        Cliente novoCliente = new Cliente(
            campoNome.getText().trim(),
            campoCpf.getText().trim(),
            campoEmail.getText().trim(),
            campoCelular.getText().trim(),
            "123" // Senha padrão
        );
        repo.adicionar(novoCliente);
        clienteAdicionado = true;
        JOptionPane.showMessageDialog(this, "Cliente adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    /**
     * Indica se um cliente foi adicionado com sucesso.
     * @return true se adicionado
     */
    public boolean isClienteAdicionado() {
        return clienteAdicionado;
    }
    
    /**
     * Cria um campo de texto estilizado com placeholder.
     */
    private JTextField createStyledTextField(String text, String placeholder) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (text.isEmpty()) {
            field.setForeground(Color.GRAY);
            field.setText(placeholder);
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(Color.BLACK);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (field.getText().isEmpty()) {
                        field.setForeground(Color.GRAY);
                        field.setText(placeholder);
                    }
                }
            });
        }
        return field;
    }

    /**
     * Cria um botão estilizado para ações principais.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
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
     * Valida os campos obrigatórios do formulário.
     */
    private boolean validarCampos() {
        if (campoNome.getText().trim().isEmpty() || campoNome.getText().equals("Nome Completo")) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (campoCpf.getText().trim().isEmpty() || campoCpf.getText().equals("CPF (somente números)")) {
            JOptionPane.showMessageDialog(this, "CPF é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (campoEmail.getText().trim().isEmpty() || campoEmail.getText().equals("E-mail")) {
            JOptionPane.showMessageDialog(this, "Email é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (campoCelular.getText().trim().isEmpty() || campoCelular.getText().equals("Celular (com DDD)")) {
            JOptionPane.showMessageDialog(this, "Celular é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
} 