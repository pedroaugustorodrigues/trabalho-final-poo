package main.java.view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Wizard para finalizar compra (passo a passo de pagamento e entrega).
 * Exibe etapas para seleção de pagamento, endereço e confirmação.
 *
 * @author Rafael
 */
public class FinalizarCompraWizard extends JDialog {
    @SuppressWarnings("unused")
    private int etapa = 1;
    private String formaPagamento = "";
    private String endereco = "";
    private double total;
    private Runnable onConfirmar;
    private Consumer<String> onConfirmarComDados;

    private JPanel mainPanel;
    private JButton btnProximo;
    private JButton btnVoltar;
    private JButton btnConfirmar;
    private JLabel lblTitulo;

    private JRadioButton rbCartao;
    private JRadioButton rbPix;
    private JRadioButton rbBoleto;
    private JTextField txtEndereco;

    /**
     * Construtor do wizard de finalização de compra.
     * @param parent janela pai
     * @param total valor total da compra
     * @param onConfirmar ação a ser executada ao confirmar
     */
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

    /**
     * Construtor do wizard de finalização de compra com callback para dados.
     * @param parent janela pai
     * @param total valor total da compra
     * @param onConfirmarComDados ação a ser executada ao confirmar com dados de pagamento e endereço
     */
    public FinalizarCompraWizard(JFrame parent, double total, Consumer<String> onConfirmarComDados) {
        super(parent, "Finalizar Compra", true);
        this.total = total;
        this.onConfirmarComDados = onConfirmarComDados;
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

    /**
     * Etapa 1: Seleção da forma de pagamento.
     */
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

    /**
     * Etapa 2: Informar endereço de entrega.
     */
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

    /**
     * Etapa 3: Confirmação do pedido.
     */
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
            if (onConfirmarComDados != null) {
                String dados = formaPagamento + "|" + endereco;
                onConfirmarComDados.accept(dados);
            }
            dispose();
        });
        south.add(btnVoltar);
        south.add(btnConfirmar);
        mainPanel.add(south, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
} 