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

public class LoginGUI extends JFrame {
    private AutenticacaoService autenticacaoService;
    private JTextField emailField;
    private JPasswordField senhaField;
    private Point initialClick;

    private static final Color COR_PAINEL_ESQUERDO = new Color(108, 99, 255);
    private static final Color COR_BOTAO_LOGIN = new Color(108, 99, 255);
    private static final Color COR_TEXTO_BOTAO = Color.WHITE;
    private static final Color COR_FUNDO_DIREITO = Color.WHITE;

    public LoginGUI(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
        setTitle("Login");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        
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

        mainPanel.add(createLeftPanel());
        mainPanel.add(createRightPanel());

        add(mainPanel);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(COR_PAINEL_ESQUERDO);
        leftPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel welcomeLabel = new JLabel("<html>Bem vindo ao<br>Sistema.</html>", SwingConstants.LEFT);
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
        
        JLabel titleLabel = new JLabel("Acessar");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        gbc.weighty = 0.1;
        rightPanel.add(titleLabel, gbc);

        emailField = new JTextField(20);
        JPanel emailPanel = createInputPanelWithIcon("/images/user-icon.png", emailField);
        gbc.weighty = 0.2;
        rightPanel.add(emailPanel, gbc);

        senhaField = new JPasswordField(20);
        JPanel senhaPanel = createInputPanelWithIcon("/images/lock-icon.png", senhaField);
        rightPanel.add(senhaPanel, gbc);

        gbc.weighty = 0.4;
        rightPanel.add(new JLabel(), gbc);

        JButton loginButton = createRoundedButton("Login");
        loginButton.addActionListener(_ -> performLogin());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 15;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.SOUTH;
        rightPanel.add(loginButton, gbc);

        return rightPanel;
    }
    
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
        button.setBackground(COR_BOTAO_LOGIN);
        button.setForeground(COR_TEXTO_BOTAO);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }
    
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
        button.addActionListener(_ -> System.exit(0));
        return button;
    }

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
            } else {
                 JOptionPane.showMessageDialog(null, "Login de cliente realizado com sucesso!", "Bem-vindo!", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (AutenticacaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Login", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
