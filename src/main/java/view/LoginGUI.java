package main.java.view;

import main.java.service.AutenticacaoService;
import main.java.model.Usuario;
import main.java.exceptions.AutenticacaoException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Tela de Login e Registro do sistema.
 * Permite alternar entre login e cadastro de usuário (Cliente ou Gestor).
 *
 * @author Pedro
 */
public class LoginGUI extends JFrame {
    private final AutenticacaoService autenticacaoService;
    private JTextField emailField;
    private JPasswordField senhaField;
    private Point initialClick;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private boolean mostrandoLogin = true;

    // Cores padrão da interface
    private static final Color COR_PAINEL_ESQUERDO = new Color(108, 99, 255);
    private static final Color COR_BOTAO_LOGIN = new Color(108, 99, 255);
    private static final Color COR_BOTAO_REGISTRO = new Color(255, 255, 255);
    private static final Color COR_TEXTO_BOTAO = Color.WHITE;
    private static final Color COR_TEXTO_BOTAO_REGISTRO = new Color(108, 99, 255);
    private static final Color COR_FUNDO_DIREITO = Color.WHITE;

    /**
     * Construtor da tela de Login.
     * @param autenticacaoService serviço de autenticação a ser utilizado
     */
    public LoginGUI(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
        setTitle("Login");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        mainPanel = new JPanel(new GridLayout(1, 2));
        leftPanel = createLeftPanel();
        rightPanel = createRightPanel();
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);
        
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
    }

    /**
     * Cria o painel esquerdo com mensagem de boas-vindas e ilustração.
     */
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(COR_PAINEL_ESQUERDO);
        leftPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel welcomeLabel = new JLabel("<html>Seja Bem-Vindo(a)</html>", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.WHITE);
        leftPanel.add(welcomeLabel, BorderLayout.NORTH);

        try {
            ImageIcon originalIcon = new ImageIcon("src/main/resources/images/illustration.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            leftPanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Imagem não carregada");
            errorLabel.setForeground(Color.RED);
            leftPanel.add(errorLabel, BorderLayout.CENTER);
        }

        return leftPanel;
    }

    /**
     * Cria o painel direito (login).
     */
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(COR_FUNDO_DIREITO);
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        JButton exitButton = createExitButton();
        gbc.anchor = GridBagConstraints.NORTHEAST;
        rightPanel.add(exitButton, gbc);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel("Acessar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        gbc.weighty = 0.1;
        rightPanel.add(titleLabel, gbc);

        emailField = new JTextField(20);
        setPlaceholder(emailField, "E-mail");
        JPanel emailPanel = createInputPanelWithIcon("/images/email-icon.png", emailField);
        gbc.weighty = 0.2;
        rightPanel.add(emailPanel, gbc);

        senhaField = new JPasswordField(20);
        setPlaceholder(senhaField, "Senha");
        JPanel senhaPanel = createInputPanelWithIcon("/images/lock-icon.png", senhaField);
        rightPanel.add(senhaPanel, gbc);

        gbc.weighty = 0.3;
        rightPanel.add(new JLabel(), gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setOpaque(false);
        JButton loginButton = createRoundedButton("Login", COR_BOTAO_LOGIN, COR_TEXTO_BOTAO);
        loginButton.addActionListener(e -> performLogin());
        buttonPanel.add(loginButton);
        JButton registerButton = createRoundedButton("Registrar", COR_BOTAO_REGISTRO, COR_TEXTO_BOTAO_REGISTRO);
        registerButton.setBorder(BorderFactory.createLineBorder(COR_BOTAO_LOGIN, 2));
        registerButton.addActionListener(e -> alternarParaRegistro());
        buttonPanel.add(registerButton);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 15;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.SOUTH;
        rightPanel.add(buttonPanel, gbc);
        return rightPanel;
    }
    
    /**
     * Cria um painel de input com ícone à esquerda.
     */
    private JPanel createInputPanelWithIcon(String iconPath, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        try {
            ImageIcon icon = new ImageIcon("src/main/resources" + iconPath);
            Image scaled = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            panel.add(new JLabel(new ImageIcon(scaled)), BorderLayout.WEST);
        } catch (Exception e) {
             panel.add(new JLabel("?"), BorderLayout.WEST);
        }
        
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }

    /**
     * Cria um botão arredondado customizado.
     */
    private JButton createRoundedButton(String text, Color backgroundColor, Color textColor) {
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
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }
    
    /**
     * Cria o botão de sair (fechar janela).
     */
    private JButton createExitButton() {
        JButton button = new JButton();
        try {
            ImageIcon icon = new ImageIcon("src/main/resources/images/power-off-icon.png");
            Image scaled = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            button.setText("X");
        }
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> System.exit(0));
        return button;
    }

    /**
     * Alterna para o painel de registro.
     */
    private void alternarParaRegistro() {
        mainPanel.remove(rightPanel);
        rightPanel = createRegisterPanel();
        mainPanel.add(rightPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        mostrandoLogin = false;
    }

    /**
     * Alterna para o painel de login.
     */
    private void alternarParaLogin() {
        mainPanel.remove(rightPanel);
        rightPanel = createRightPanel();
        mainPanel.add(rightPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        mostrandoLogin = true;
    }

    /**
     * Cria o painel de registro de usuário.
     */
    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(COR_FUNDO_DIREITO);
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JButton exitButton = createExitButton();
        gbc.anchor = GridBagConstraints.NORTHEAST;
        registerPanel.add(exitButton, gbc);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel("Registrar Conta", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        gbc.weighty = 0.1;
        registerPanel.add(titleLabel, gbc);
        JTextField nomeField = new JTextField(20);
        setPlaceholder(nomeField, "Nome completo");
        JPanel nomePanel = createInputPanelWithIcon("/images/user-icon.png", nomeField);
        gbc.weighty = 0.2;
        registerPanel.add(nomePanel, gbc);
        JTextField cpfField = new JTextField(20);
        setPlaceholder(cpfField, "CPF");
        JPanel cpfPanel = createInputPanelWithIcon("/images/user-icon.png", cpfField);
        gbc.weighty = 0.2;
        registerPanel.add(cpfPanel, gbc);
        JTextField emailField = new JTextField(20);
        setPlaceholder(emailField, "E-mail");
        JPanel emailPanel = createInputPanelWithIcon("/images/email-icon.png", emailField);
        registerPanel.add(emailPanel, gbc);
        JPasswordField senhaField = new JPasswordField(20);
        setPlaceholder(senhaField, "Senha");
        JPanel senhaPanel = createInputPanelWithIcon("/images/lock-icon.png", senhaField);
        registerPanel.add(senhaPanel, gbc);
        JPasswordField confirmarSenhaField = new JPasswordField(20);
        setPlaceholder(confirmarSenhaField, "Confirmar senha");
        JPanel confirmarSenhaPanel = createInputPanelWithIcon("/images/lock-icon.png", confirmarSenhaField);
        registerPanel.add(confirmarSenhaPanel, gbc);
        JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipoPanel.setOpaque(false);
        JLabel tipoLabel = new JLabel("Tipo de Usuário:");
        tipoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JRadioButton rbCliente = new JRadioButton("Cliente");
        JRadioButton rbGestor = new JRadioButton("Gestor");
        rbCliente.setBackground(COR_FUNDO_DIREITO);
        rbGestor.setBackground(COR_FUNDO_DIREITO);
        ButtonGroup tipoGroup = new ButtonGroup();
        tipoGroup.add(rbCliente);
        tipoGroup.add(rbGestor);
        tipoPanel.add(tipoLabel);
        tipoPanel.add(rbCliente);
        tipoPanel.add(rbGestor);
        gbc.weighty = 0.1;
        registerPanel.add(tipoPanel, gbc);
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setOpaque(false);
        JButton cadastrarButton = createRoundedButton("Cadastrar", COR_BOTAO_LOGIN, COR_TEXTO_BOTAO);
        cadastrarButton.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String cpf = cpfField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(senhaField.getPassword());
            String confirmarSenha = new String(confirmarSenhaField.getPassword());
            String tipoUsuario = rbGestor.isSelected() ? "gestor" : "cliente";

            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!senha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (senha.length() < 4) {
                JOptionPane.showMessageDialog(this, "A senha deve ter pelo menos 4 caracteres.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                autenticacaoService.cadastrarUsuario(nome, cpf, email, senha, tipoUsuario);
                alternarParaLogin();
                JOptionPane.showMessageDialog(this, "Conta cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(cadastrarButton);
        JButton voltarButton = createRoundedButton("Já tem conta? Acessar", COR_BOTAO_REGISTRO, COR_TEXTO_BOTAO_REGISTRO);
        voltarButton.setBorder(BorderFactory.createLineBorder(COR_BOTAO_LOGIN, 2));
        voltarButton.addActionListener(e -> alternarParaLogin());
        buttonPanel.add(voltarButton);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 15;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.SOUTH;
        registerPanel.add(buttonPanel, gbc);
        return registerPanel;
    }

    /**
     * Adiciona placeholder a JTextField ou JPasswordField.
     */
    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar('•');
                    }
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar((char) 0);
                    }
                }
            }
        });
        if (field instanceof JPasswordField) {
            ((JPasswordField) field).setEchoChar((char) 0);
        }
    }

    /**
     * Realiza o login do usuário.
     */
    private void performLogin() {
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro de Login", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuarioLogado = autenticacaoService.login(email, senha);
            
            dispose();

            if (usuarioLogado instanceof main.java.model.Gestor) {
                new GestorDashboardGUI(usuarioLogado).setVisible(true);
            } else if (usuarioLogado instanceof main.java.model.Cliente) {
                new ClienteDashboardGUI(usuarioLogado).setVisible(true);
            }

        } catch (AutenticacaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Login", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
