package main.java.view;

import main.java.service.AutenticacaoService;
import main.java.exceptions.RegistroDuplicadoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGUI extends JDialog {
    private AutenticacaoService autenticacaoService;
    private JFrame parentFrame;
    private JTextField nomeField;
    private JTextField emailField;
    private JPasswordField senhaField;
    private JComboBox<String> tipoUsuarioComboBox;
    private JButton registerButton;
    private JButton backButton;

    public RegisterGUI(AutenticacaoService autenticacaoService, JFrame parentFrame) {
        super(parentFrame, "Sistema de Vendas de Eletrônicos - Cadastro", true);
        this.autenticacaoService = autenticacaoService;
        this.parentFrame = parentFrame;
        setSize(400, 350);
        setLocationRelativeTo(parentFrame);

        initComponents();
        addListeners();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nomeField = new JTextField(20);
        panel.add(nomeField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        senhaField = new JPasswordField(20);
        panel.add(senhaField, gbc);

        // Tipo de Usuário
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Tipo de Usuário:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        tipoUsuarioComboBox = new JComboBox<>(new String[]{"Cliente", "Gestor"});
        panel.add(tipoUsuarioComboBox, gbc);

        // Botão Cadastrar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton = new JButton("Cadastrar");
        panel.add(registerButton, gbc);

        // Botão Voltar
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        backButton = new JButton("Voltar ao Login");
        panel.add(backButton, gbc);

        add(panel);
    }

    private void addListeners() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                parentFrame.setVisible(true);
            }
        });
    }

    private void performRegistration() {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());
        String tipo = (String) tipoUsuarioComboBox.getSelectedItem();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro de Cadastro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            autenticacaoService.cadastrarUsuario(nome, email, senha, tipo.toLowerCase());
            JOptionPane.showMessageDialog(this, tipo + " " + nome + " cadastrado com sucesso! Você já pode fazer login.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            parentFrame.setVisible(true);
        } catch (RegistroDuplicadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
