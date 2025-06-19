package main.java.view;

import main.java.service.AutenticacaoService;
import main.java.model.Usuario;
import main.java.exceptions.AutenticacaoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private AutenticacaoService autenticacaoService;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginGUI(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
        setTitle("Sistema de Vendas de Eletrônicos - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        addListeners();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        senhaField = new JPasswordField(20);
        panel.add(senhaField, gbc);

        // Botão Login
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Ocupa duas colunas
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        // Botão Cadastre-se
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton = new JButton("Não tem conta? Cadastre-se!");
        panel.add(registerButton, gbc);

        add(panel);
    }

    private void addListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegisterScreen();
            }
        });
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
            JOptionPane.showMessageDialog(this, "Login bem-sucedido! Bem-vindo, " + usuarioLogado.getNome() + " (" + usuarioLogado.getTipoUsuario() + ")", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            if (usuarioLogado instanceof main.java.model.Cliente) {
                JOptionPane.showMessageDialog(this, "Redirecionando para a tela do Cliente.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            } else if (usuarioLogado instanceof main.java.model.Gestor) {
                JOptionPane.showMessageDialog(this, "Redirecionando para a tela do Gestor.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (AutenticacaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void openRegisterScreen() {
        RegisterGUI registerGUI = new RegisterGUI(autenticacaoService, null);
        registerGUI.setVisible(true);
        this.setVisible(false);
    }
}
