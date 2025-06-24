package main.java.view;

import main.java.service.AutenticacaoService;
import main.java.exceptions.RegistroDuplicadoException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RegisterGUI extends JDialog {
    private AutenticacaoService autenticacaoService;
    private JTextField nomeField;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JPasswordField confirmarSenhaField;
    private ButtonGroup tipoUsuarioGroup;
    private JRadioButton gestorRadioButton;
    private JRadioButton clienteRadioButton;
    private Point initialClick;
    private JTextField cpfField;

    private static final Color COR_PRINCIPAL = new Color(108, 99, 255);
    private static final Color COR_BOTAO = new Color(108, 99, 255);
    private static final Color COR_TEXTO_BOTAO = Color.WHITE;
    private static final Color COR_FUNDO = Color.WHITE;

    public RegisterGUI(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
        
        setTitle("Registro de Usuário");
        setSize(500, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setModal(true);
        setUndecorated(true);
        setResizable(false);

        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        // Adicionar funcionalidade de arrastar a janela
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COR_FUNDO);
        mainPanel.setBorder(BorderFactory.createLineBorder(COR_PRINCIPAL, 2));

        // Cabeçalho
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Conteúdo
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    @SuppressWarnings("unused")
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COR_PRINCIPAL);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Criar Conta");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(COR_PRINCIPAL);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        headerPanel.add(closeButton, BorderLayout.EAST);

        return headerPanel;
    }

    @SuppressWarnings("unused")
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(COR_FUNDO);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPanel.add(nomeLabel, gbc);

        nomeField = new JTextField(20);
        nomeField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nomeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        contentPanel.add(nomeField, gbc);

        // CPF
        JLabel cpfLabel = new JLabel("CPF:");
        cpfLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPanel.add(cpfLabel, gbc);

        cpfField = new JTextField(20);
        cpfField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cpfField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        contentPanel.add(cpfField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPanel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        contentPanel.add(emailField, gbc);

        // Senha
        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPanel.add(senhaLabel, gbc);

        senhaField = new JPasswordField(20);
        senhaField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        senhaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        contentPanel.add(senhaField, gbc);

        // Confirmar Senha
        JLabel confirmarSenhaLabel = new JLabel("Confirmar Senha:");
        confirmarSenhaLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPanel.add(confirmarSenhaLabel, gbc);

        confirmarSenhaField = new JPasswordField(20);
        confirmarSenhaField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        confirmarSenhaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        contentPanel.add(confirmarSenhaField, gbc);

        // Tipo de Usuário
        JLabel tipoLabel = new JLabel("Tipo de Usuário:");
        tipoLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPanel.add(tipoLabel, gbc);

        JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipoPanel.setOpaque(false);

        tipoUsuarioGroup = new ButtonGroup();
        gestorRadioButton = new JRadioButton("Gestor");
        clienteRadioButton = new JRadioButton("Cliente");
        
        gestorRadioButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        clienteRadioButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        tipoUsuarioGroup.add(gestorRadioButton);
        tipoUsuarioGroup.add(clienteRadioButton);
        
        // Definir Cliente como padrão
        clienteRadioButton.setSelected(true);
        
        tipoPanel.add(gestorRadioButton);
        tipoPanel.add(clienteRadioButton);
        contentPanel.add(tipoPanel, gbc);

        // Espaçamento
        gbc.weighty = 1.0;
        contentPanel.add(new JLabel(), gbc);
        gbc.weighty = 0.0;

        // Botão Registrar
        JButton registerButton = createRoundedButton("Registrar");
        registerButton.addActionListener(e -> performRegister());
        gbc.ipady = 15;
        contentPanel.add(registerButton, gbc);

        return contentPanel;
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(getForeground());
                g2.drawString(getText(), (getWidth() - g.getFontMetrics().stringWidth(getText())) / 2, (getHeight() + g.getFontMetrics().getAscent()) / 2 - 2);
                g2.dispose();
            }
        };
        button.setBackground(COR_BOTAO);
        button.setForeground(COR_TEXTO_BOTAO);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }

    private void performRegister() {
        String nome = nomeField.getText().trim();
        String cpf = cpfField.getText().trim();
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword());
        String confirmarSenha = new String(confirmarSenhaField.getPassword());

        // Validações
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos.", 
                "Erro de Validação", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, 
                "As senhas não coincidem.", 
                "Erro de Validação", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (senha.length() < 4) {
            JOptionPane.showMessageDialog(this, 
                "A senha deve ter pelo menos 4 caracteres.", 
                "Erro de Validação", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!gestorRadioButton.isSelected() && !clienteRadioButton.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, selecione um tipo de usuário.", 
                "Erro de Validação", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha o CPF.", 
                "Erro de Validação", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipoUsuario = gestorRadioButton.isSelected() ? "gestor" : "cliente";

        try {
            autenticacaoService.cadastrarUsuario(nome, cpf, email, senha, tipoUsuario);
            
            JOptionPane.showMessageDialog(this, 
                "Usuário cadastrado com sucesso!\nVocê pode fazer login agora.", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose(); // Fecha a janela de registro
            
        } catch (RegistroDuplicadoException ex) {
            JOptionPane.showMessageDialog(this, 
                ex.getMessage(), 
                "Erro de Registro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Ocorreu um erro inesperado: " + ex.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
