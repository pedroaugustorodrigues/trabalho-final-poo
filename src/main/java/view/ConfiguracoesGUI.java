package main.java.view;

import main.java.model.Usuario;
import main.java.repository.UsuarioRepository;
import main.java.service.AutenticacaoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ConfiguracoesGUI extends JPanel {
    private Usuario usuario;
    private UsuarioRepository usuarioRepository;
    private AutenticacaoService autenticacaoService;
    
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private final Color CARD_BG = Color.WHITE;
    private final Color PRIMARY_COLOR = new Color(108, 99, 255);
    private final Color ACCENT_COLOR = new Color(32, 201, 151);
    private final Color DANGER_COLOR = new Color(230, 86, 86);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color SECONDARY_TEXT = new Color(120, 120, 120);

    public ConfiguracoesGUI(Usuario usuario) {
        this.usuario = usuario;
        this.usuarioRepository = new UsuarioRepository();
        this.autenticacaoService = new AutenticacaoService(usuarioRepository);
        
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Configuracoes");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createUserInfoSection());
        mainPanel.add(Box.createVerticalStrut(30));
        
        mainPanel.add(createSystemOptionsSection());

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_COLOR;
                this.trackColor = new Color(240, 240, 240);
            }
        });

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createUserInfoSection() {
        JPanel section = new JPanel();
        section.setLayout(new BorderLayout());
        section.setBackground(CARD_BG);
        section.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("Informacoes do Usuario");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_COLOR);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        section.add(sectionTitle, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 15, 10));
        infoPanel.setBackground(CARD_BG);

        infoPanel.add(createInfoLabel("Nome:"));
        infoPanel.add(createInfoValue(usuario.getNome()));
        infoPanel.add(createInfoLabel("Email:"));
        infoPanel.add(createInfoValue(usuario.getEmail()));
        infoPanel.add(createInfoLabel("Tipo:"));
        infoPanel.add(createInfoValue(usuario instanceof main.java.model.Gestor ? "Gestor" : "Cliente"));

        section.add(infoPanel, BorderLayout.CENTER);
        return section;
    }

    @SuppressWarnings("unused")
    private JPanel createSystemOptionsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BorderLayout());
        section.setBackground(CARD_BG);
        section.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("Opcoes do Sistema");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_COLOR);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        section.add(sectionTitle, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(CARD_BG);

        JButton btnAlterarSenha = createStyledButton("Alterar Senha", ACCENT_COLOR, "🔒");
        btnAlterarSenha.addActionListener(e -> alterarSenha());
        buttonPanel.add(btnAlterarSenha);

        JButton btnSair = createStyledButton("Sair do Sistema", DANGER_COLOR, "🚪");
        btnSair.addActionListener(e -> sairDoSistema());
        buttonPanel.add(btnSair);

        section.add(buttonPanel, BorderLayout.CENTER);
        return section;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JLabel createInfoValue(String value) {
        JLabel label = new JLabel(value);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(SECONDARY_TEXT);
        return label;
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

    @SuppressWarnings("unused")
    private void alterarSenha() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Alterar Senha", true);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CARD_BG);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Alterar Senha");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));

        JPasswordField campoSenhaAtual = createStyledPasswordField("Senha Atual");
        JPasswordField campoNovaSenha = createStyledPasswordField("Nova Senha");
        JPasswordField campoConfirmarSenha = createStyledPasswordField("Confirmar Nova Senha");

        mainPanel.add(campoSenhaAtual);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(campoNovaSenha);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(campoConfirmarSenha);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(CARD_BG);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        JButton btnSalvar = createStyledButton("Salvar", ACCENT_COLOR, "💾");
        JButton btnCancelar = createStyledButton("Cancelar", DANGER_COLOR, "❌");

        btnSalvar.addActionListener(e -> {
            String senhaAtual = new String(campoSenhaAtual.getPassword());
            String novaSenha = new String(campoNovaSenha.getPassword());
            String confirmarSenha = new String(campoConfirmarSenha.getPassword());
            
            // Verificar se a senha atual esta correta
            if (!autenticacaoService.autenticar(usuario.getEmail(), senhaAtual)) {
                JOptionPane.showMessageDialog(dialog, "Senha atual incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verificar se a nova senha nao esta vazia
            if (novaSenha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nova senha e obrigatoria!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verificar se as senhas coincidem
            if (!novaSenha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(dialog, "As senhas nao coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verificar se a nova senha e diferente da atual
            if (senhaAtual.equals(novaSenha)) {
                JOptionPane.showMessageDialog(dialog, "A nova senha deve ser diferente da atual!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Atualizar a senha
            usuario.setSenha(novaSenha);
            usuarioRepository.atualizarUsuario(usuario);
            
            JOptionPane.showMessageDialog(dialog, "Senha alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Placeholder effect
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('•');
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getPassword().length == 0) {
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        
        return field;
    }

    private void sairDoSistema() {
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja sair do sistema?", 
            "Confirmar Saida", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
} 