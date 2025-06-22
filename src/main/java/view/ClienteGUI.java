package main.java.view;

import main.java.model.Cliente;
import main.java.repository.ClienteRepository;

import javax.swing.*;
import java.awt.*;

public class ClienteGUI extends JFrame {
    private ClienteRepository repo = new ClienteRepository();
    private DefaultListModel<Cliente> listModel = new DefaultListModel<>();
    private JList<Cliente> listaClientes = new JList<>(listModel);

    public ClienteGUI() {
        setTitle("Gerenciar Clientes");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");

        btnAdicionar.addActionListener(e -> adicionarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnRemover.addActionListener(e -> removerCliente());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);

        add(new JScrollPane(listaClientes), BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        carregarClientes();
    }

    private void carregarClientes() {
        listModel.clear();
        for (Cliente c : repo.listar()) {
            listModel.addElement(c);
        }
    }

    private void adicionarCliente() {
        Cliente c = dialogCliente(null);
        if (c != null) {
            repo.adicionar(c);
            listModel.addElement(c);
        }
    }

    private void editarCliente() {
        Cliente selecionado = listaClientes.getSelectedValue();
        if (selecionado != null) {
            Cliente atualizado = dialogCliente(selecionado);
            if (atualizado != null) {
                repo.atualizar(atualizado);
                carregarClientes();
            }
        }
    }

    private void removerCliente() {
        Cliente selecionado = listaClientes.getSelectedValue();
        if (selecionado != null) {
            repo.removerPorCpf(selecionado.getCpf());
            listModel.removeElement(selecionado);
        }
    }

    private Cliente dialogCliente(Cliente c) {
        JTextField campoNome = new JTextField(c != null ? c.getNome() : "");
        JTextField campoCpf = new JTextField(c != null ? c.getCpf() : "");
        JTextField campoEmail = new JTextField(c != null ? c.getEmail() : "");
        JTextField campoCelular = new JTextField(c != null ? c.getCelular() : "");

        if (c != null) campoCpf.setEditable(false);

        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("CPF:"));
        painel.add(campoCpf);
        painel.add(new JLabel("Email:"));
        painel.add(campoEmail);
        painel.add(new JLabel("Celular:"));
        painel.add(campoCelular);

        int resultado = JOptionPane.showConfirmDialog(this, painel, "Dados do Cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            // Usa a senha antiga, pois não é alterada
            String senha = c != null ? c.getSenha() : "123"; // pode ser "null" ou algum valor padrão, dependendo do seu controle
            return new Cliente(
                    campoNome.getText(),
                    campoCpf.getText(),
                    campoEmail.getText(),
                    campoCelular.getText(),
                    senha
            );
        } else {
            return null;
        }
    }
}
