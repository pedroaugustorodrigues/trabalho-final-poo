package main.java.view;

import javax.swing.*;
import java.awt.*;

public class FinalizarCompraWizard extends JDialog {
    @SuppressWarnings("unused")
    private int etapa = 1;
    private String formaPagamento = "";
    private String endereco = "";
    private double total;
    private Runnable onConfirmar;

    private JPanel mainPanel;
    private JButton btnProximo;
    private JButton btnVoltar;
    private JButton btnConfirmar;
    private JLabel lblTitulo;

    private JRadioButton rbCartao;
    private JRadioButton rbPix;
    private JRadioButton rbBoleto;
    private JTextField txtEndereco;

    public FinalizarCompraWizard(JFrame parent, double total, Runnable onConfirmar) {
        super(parent, "Finalizar Compra", true);
        this.total = total;
        this.onConfirmar = onConfirmar;
        setSize(480, 340);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        mainPanel.setBackground(Color.WHITE);
        lblTitulo = new JLabel("Selecione a forma de pagamento", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(30, 41, 59));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);
        add(mainPanel);
        criarEtapa1();
    }

    @SuppressWarnings("unused")
    private void criarEtapa1() {
        mainPanel.removeAll();
        lblTitulo.setText("Selecione a forma de pagamento");
        mainPanel.add(lblTitulo, BorderLayout.NORTH);
        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();
        rbCartao = new JRadioButton("Cartão de Crédito");
        rbPix = new JRadioButton("Pix");
        rbBoleto = new JRadioButton("Boleto Bancário");
        rbCartao.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rbPix.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rbBoleto.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rbCartao.setBackground(Color.WHITE);
        rbPix.setBackground(Color.WHITE);
        rbBoleto.setBackground(Color.WHITE);
        group.add(rbCartao);
        group.add(rbPix);
        group.add(rbBoleto);
        center.add(Box.createVerticalStrut(20));
        center.add(rbCartao);
        center.add(Box.createVerticalStrut(10));
        center.add(rbPix);
        center.add(Box.createVerticalStrut(10));
        center.add(rbBoleto);
        mainPanel.add(center, BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(Color.WHITE);
        btnProximo = new JButton("Próximo");
        btnProximo.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnProximo.addActionListener(e -> {
            if (!rbCartao.isSelected() && !rbPix.isSelected() && !rbBoleto.isSelected()) {
                JOptionPane.showMessageDialog(this, "Selecione uma forma de pagamento.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (rbCartao.isSelected()) formaPagamento = "Cartão de Crédito";
            if (rbPix.isSelected()) formaPagamento = "Pix";
            if (rbBoleto.isSelected()) formaPagamento = "Boleto Bancário";
            criarEtapa2();
        });
        south.add(btnProximo);
        mainPanel.add(south, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    @SuppressWarnings("unused")
    private void criarEtapa2() {
        mainPanel.removeAll();
        lblTitulo.setText("Informe o endereço de entrega");
        mainPanel.add(lblTitulo, BorderLayout.NORTH);
        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        txtEndereco = new JTextField();
        txtEndereco.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtEndereco.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        center.add(Box.createVerticalStrut(20));
        center.add(new JLabel("Endereço completo:"));
        center.add(Box.createVerticalStrut(8));
        center.add(txtEndereco);
        mainPanel.add(center, BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(Color.WHITE);
        btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btnVoltar.addActionListener(e -> criarEtapa1());
        btnProximo = new JButton("Próximo");
        btnProximo.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnProximo.addActionListener(e -> {
            if (txtEndereco.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite o endereço de entrega.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            endereco = txtEndereco.getText().trim();
            criarEtapa3();
        });
        south.add(btnVoltar);
        south.add(btnProximo);
        mainPanel.add(south, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    @SuppressWarnings("unused")
    private void criarEtapa3() {
        mainPanel.removeAll();
        lblTitulo.setText("Confirmação do Pedido");
        mainPanel.add(lblTitulo, BorderLayout.NORTH);
        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(20));
        center.add(new JLabel("Forma de Pagamento: " + formaPagamento));
        center.add(Box.createVerticalStrut(10));
        center.add(new JLabel("Endereço: " + endereco));
        center.add(Box.createVerticalStrut(10));
        JLabel totalLabel = new JLabel("Total: R$ " + String.format("%.2f", total));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalLabel.setForeground(new Color(99, 102, 241));
        center.add(totalLabel);
        mainPanel.add(center, BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(Color.WHITE);
        btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btnVoltar.addActionListener(e -> criarEtapa2());
        btnConfirmar = new JButton("Confirmar Pedido");
        btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnConfirmar.addActionListener(e -> {
            if (onConfirmar != null) onConfirmar.run();
            JOptionPane.showMessageDialog(this, "Compra finalizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        south.add(btnVoltar);
        south.add(btnConfirmar);
        mainPanel.add(south, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
} 