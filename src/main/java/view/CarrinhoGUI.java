package main.java.view;

import main.java.model.Carrinho;
import main.java.model.CarrinhoItem;
import main.java.model.Produto;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CarrinhoGUI extends JFrame {
    private Carrinho carrinho;
    private JPanel itensPanel;
    private JLabel totalLabel;

    private Color roxoTopo = new Color(108, 99, 255);
    private Color azulEscuro = new Color(40, 40, 106);
    private Color cinzaClaro = new Color(245, 245, 245);

    public CarrinhoGUI(Carrinho carrinho) {
        this.carrinho = carrinho;

        setTitle("Carrinho");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(cinzaClaro);
        setLayout(new BorderLayout());

        add(criarTopo(), BorderLayout.NORTH);
        add(criarListaItens(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        atualizarLista();
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(roxoTopo);
        header.setPreferredSize(new Dimension(getWidth(), 100));
        JLabel titulo = new JLabel("Nome da loja");
        titulo.setBorder(BorderFactory.createEmptyBorder(23, 20, 0, 0));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        header.add(titulo);

        JPanel subtitulo = new JPanel(null);
        subtitulo.setBackground(cinzaClaro);
        subtitulo.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel carrinhoLabel = new JLabel("SEU CARRINHO");
        carrinhoLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
        carrinhoLabel.setForeground(Color.BLACK);

        JButton voltarBtn = new RoundedButton("\u2190 Voltar", 20);
        voltarBtn.setPreferredSize(new Dimension(200, 40));
        voltarBtn.setBackground(azulEscuro);
        voltarBtn.setForeground(Color.WHITE);
        voltarBtn.addActionListener(e -> dispose());

        subtitulo.add(carrinhoLabel);
        subtitulo.add(voltarBtn);

        subtitulo.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int w = subtitulo.getWidth();
                int h = subtitulo.getHeight();

                Dimension lblSize = carrinhoLabel.getPreferredSize();
                carrinhoLabel.setBounds((w - lblSize.width) / 2, (h - lblSize.height) / 2, lblSize.width, lblSize.height);

                Dimension btnSize = voltarBtn.getPreferredSize();
                voltarBtn.setBounds(15, (h - btnSize.height) / 2, btnSize.width, btnSize.height);
            }
        });

        topo.add(header);
        topo.add(Box.createRigidArea(new Dimension(0, 22)));
        topo.add(subtitulo);

        return topo;
    }

    private JScrollPane criarListaItens() {
        itensPanel = new JPanel();
        itensPanel.setLayout(new BoxLayout(itensPanel, BoxLayout.Y_AXIS));
        itensPanel.setBackground(cinzaClaro);

        JScrollPane scroll = new JScrollPane(itensPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(cinzaClaro);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(cinzaClaro);

        totalLabel = new JLabel("Total: R$ 0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton finalizarBtn = new RoundedButton("Finalizar Compra", 20);
        finalizarBtn.setBackground(roxoTopo);
        finalizarBtn.setForeground(Color.WHITE);
        finalizarBtn.setPreferredSize(new Dimension(200, 40));
        finalizarBtn.addActionListener(e -> abrirModalPagamento());

        rodape.add(totalLabel, BorderLayout.WEST);
        JPanel painelFinalizar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelFinalizar.setOpaque(false);
        painelFinalizar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        painelFinalizar.add(finalizarBtn);

        rodape.add(painelFinalizar, BorderLayout.EAST);

        return rodape;
    }

    private void abrirModalPagamento() {
        if (carrinho.isVazio()) {
            JOptionPane.showMessageDialog(this, "Seu carrinho está vazio.");
            return;
        }

        JDialog modal = new JDialog(this, "Forma de Pagamento", true);
        modal.setSize(400, 350);
        modal.setLayout(new BorderLayout());
        modal.setLocationRelativeTo(this);

        JPanel painel = new JPanel();
        painel.setBackground(Color.WHITE);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Selecione a forma de pagamento:", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        painel.add(titulo);

        ButtonGroup grupo = new ButtonGroup();

        // Painel dos botões (horizontal e centralizado)
        JPanel botoesPanel = new JPanel();
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.setLayout(new BoxLayout(botoesPanel, BoxLayout.X_AXIS));
        botoesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pix
        ImageIcon pixIcon = new ImageIcon(new ImageIcon("C:\\Users\\Rafael\\OneDrive\\Documentos\\trabalhoFinal-POO\\trabalho-final-poo\\src\\main\\resources\\images\\logo-pix-icone-1024.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        JLabel imgPix = new JLabel(pixIcon);
        JToggleButton btnPix = new JToggleButton("Pix");
        btnPix.setBackground(azulEscuro);
        btnPix.setForeground(Color.WHITE);
        btnPix.setFocusPainted(false);
        btnPix.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box pixBox = Box.createVerticalBox();
        pixBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        pixBox.add(imgPix);
        pixBox.add(Box.createVerticalStrut(5));
        pixBox.add(btnPix);

        // Cartão
        ImageIcon cartaoIcon = new ImageIcon(new ImageIcon("C:\\Users\\Rafael\\OneDrive\\Documentos\\trabalhoFinal-POO\\trabalho-final-poo\\src\\main\\resources\\images\\vector-credit-card-icon.jpg").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        JLabel imgCartao = new JLabel(cartaoIcon);
        JToggleButton btnCartao = new JToggleButton("Cartão");
        btnCartao.setBackground(azulEscuro);
        btnCartao.setForeground(Color.WHITE);
        btnCartao.setFocusPainted(false);
        btnCartao.setAlignmentX(Component.CENTER_ALIGNMENT);

        Box cartaoBox = Box.createVerticalBox();
        cartaoBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        cartaoBox.add(imgCartao);
        cartaoBox.add(Box.createVerticalStrut(5));
        cartaoBox.add(btnCartao);

        grupo.add(btnPix);
        grupo.add(btnCartao);

        botoesPanel.add(Box.createHorizontalGlue());
        botoesPanel.add(pixBox);
        botoesPanel.add(Box.createHorizontalStrut(40)); // espaço entre os blocos
        botoesPanel.add(cartaoBox);
        botoesPanel.add(Box.createHorizontalGlue());

        painel.add(botoesPanel);

        // Botão de confirmação
        JButton confirmar = new JButton("Finalizar Compra");
        confirmar.setBackground(azulEscuro);
        confirmar.setForeground(Color.WHITE);
        confirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmar.addActionListener(e -> {
            if (!btnPix.isSelected() && !btnCartao.isSelected()) {
                JOptionPane.showMessageDialog(modal, "Selecione uma forma de pagamento.");
                return;
            }
            double total = carrinho.getTotal();
            carrinho.limpar();
            atualizarLista();
            modal.dispose();
            JOptionPane.showMessageDialog(this, "Compra finalizada! Total pago: R$ " + String.format("%.2f", total), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        painel.add(Box.createVerticalStrut(20));
        painel.add(confirmar);
        painel.add(Box.createVerticalStrut(10));

        modal.add(painel, BorderLayout.CENTER);
        modal.setVisible(true);
    }



    private JToggleButton criarBotaoForma(String texto, String caminhoImg) {
        ImageIcon icon = new ImageIcon(caminhoImg);
        Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(img));

        JToggleButton botao = new JToggleButton(texto);
        botao.setIcon(new ImageIcon(img));
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setBackground(azulEscuro);
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(120, 100));

        return botao;
    }

    private void atualizarLista() {
        itensPanel.removeAll();
        itensPanel.add(Box.createVerticalStrut(20));

        for (CarrinhoItem item : carrinho.getItens()) {
            itensPanel.add(criarCardItem(item));
            itensPanel.add(Box.createVerticalStrut(10));
        }

        totalLabel.setText("Total: R$ " + String.format("%.2f", carrinho.getTotal()));
        itensPanel.revalidate();
        itensPanel.repaint();
    }

    private JPanel criarCardItem(CarrinhoItem item) {
        RoundedPanel card = new RoundedPanel(20);
        card.setLayout(new BoxLayout(card, BoxLayout.X_AXIS));
        card.setBackground(Color.WHITE);

        Dimension dim = new Dimension(1000, 80);
        card.setPreferredSize(dim);
        card.setMinimumSize(dim);
        card.setMaximumSize(dim);

        JLabel imgLabel;
        try {
            ImageIcon icon = new ImageIcon(item.getProduto().getCaminhoImagem());
            Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            imgLabel = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            imgLabel = new JLabel("Sem imagem");
        }
        imgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel desc = new JLabel(item.getProduto().getDescricao() + " x" + item.getQuantidade());
        desc.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JLabel preco = new JLabel("R$ " + String.format("%.2f", item.getSubtotal()));
        preco.setFont(new Font("SansSerif", Font.BOLD, 16));
        preco.setForeground(new Color(40, 40, 40));

        JButton remover = new RoundedButton("Remover", 15);
        remover.setBackground(Color.RED);
        remover.setForeground(Color.WHITE);
        remover.addActionListener(e -> {
            carrinho.removerProduto(item.getProduto());
            atualizarLista();
        });

        card.add(Box.createHorizontalStrut(10));
        card.add(imgLabel);
        card.add(Box.createHorizontalStrut(10));
        card.add(desc);
        card.add(Box.createHorizontalGlue());
        card.add(preco);
        card.add(Box.createHorizontalStrut(10));
        card.add(remover);
        card.add(Box.createHorizontalStrut(10));

        return card;
    }

    public class RoundedPanel extends JPanel {
        private int radius;
        private int shadowSize = 5;
        private Color shadowColor = new Color(0, 0, 0, 30);

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - shadowSize;
            int height = getHeight() - shadowSize;

            RoundRectangle2D.Float shadow = new RoundRectangle2D.Float(shadowSize, shadowSize, width, height, radius, radius);
            g2.setColor(shadowColor);
            g2.fill(shadow);

            RoundRectangle2D.Float background = new RoundRectangle2D.Float(0, 0, width, height, radius, radius);
            g2.setColor(getBackground());
            g2.fill(background);

            g2.dispose();
        }
    }

    public class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape round = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(getBackground());
            g2.fill(round);

            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
        }
    }
}
