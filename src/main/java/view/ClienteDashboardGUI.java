package main.java.view;

import main.java.model.Carrinho;
import main.java.model.Produto;
import main.java.repository.ProdutoRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.geom.RoundRectangle2D;

public class ClienteDashboardGUI extends JFrame {
    private JPanel produtosPanel;
    private ProdutoRepository produtoRepository;
    private Carrinho carrinho;
    Color roxoTopo = new Color(108, 99, 255);
    Color azulEscuro = new Color(40, 40, 106);
    Color cinzaClaro = new Color(245, 245, 245);

    public ClienteDashboardGUI() {
        produtoRepository = new ProdutoRepository();
        carrinho = new Carrinho();

        setTitle("Loja - Cliente");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(cinzaClaro);

        // Layout fluido horizontal com quebra automática
        produtosPanel = new JPanel();
        produtosPanel.setLayout(new GridBagLayout());
        produtosPanel.setBackground(cinzaClaro);
        produtosPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));
        produtosPanel.setPreferredSize(new Dimension(1100, 0)); // largura que comporta 4 cards de 250px + espaçamento


        JScrollPane scrollPane = new JScrollPane(produtosPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(cinzaClaro);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        add(criarTopoCompleto(), BorderLayout.NORTH);
        carregarProdutos();
    }

    private JPanel criarTopoCompleto() {
        JPanel topoCompleto = new JPanel();
        topoCompleto.setLayout(new BoxLayout(topoCompleto, BoxLayout.Y_AXIS));
        topoCompleto.setBackground(cinzaClaro);

        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTitulo.setBackground(roxoTopo);
        painelTitulo.setPreferredSize(new Dimension(getWidth(), 100));
        JLabel tituloLabel = new JLabel("Nome da loja");
        tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        tituloLabel.setForeground(Color.WHITE);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(23, 0, 0, 0));
        painelTitulo.add(tituloLabel);

        JPanel painelProdutosBotao = new JPanel(null);
        painelProdutosBotao.setBackground(cinzaClaro);
        painelProdutosBotao.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel produtosLabel = new JLabel("PRODUTOS DISPONÍVEIS");
        produtosLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
        produtosLabel.setForeground(Color.BLACK);

        JButton verCarrinhoBtn = new RoundedButton("🛒 Ver Carrinho", 20);
        verCarrinhoBtn.setPreferredSize(new Dimension(150, 40));
        verCarrinhoBtn.setBackground(azulEscuro);
        verCarrinhoBtn.setForeground(Color.WHITE);
        verCarrinhoBtn.setFocusPainted(false);
        verCarrinhoBtn.setOpaque(true);
        verCarrinhoBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        verCarrinhoBtn.addActionListener(e -> {
            CarrinhoGUI carrinhoGUI = new CarrinhoGUI(carrinho);
            carrinhoGUI.setVisible(true);
        });

        painelProdutosBotao.add(produtosLabel);
        painelProdutosBotao.add(verCarrinhoBtn);

        painelProdutosBotao.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int width = painelProdutosBotao.getWidth();
                int height = painelProdutosBotao.getHeight();

                Dimension labelSize = produtosLabel.getPreferredSize();
                produtosLabel.setBounds((width - labelSize.width) / 2, (height - labelSize.height) / 2, labelSize.width, labelSize.height);

                Dimension buttonSize = verCarrinhoBtn.getPreferredSize();
                verCarrinhoBtn.setBounds(width - buttonSize.width - 15, (height - buttonSize.height) / 2, buttonSize.width, buttonSize.height);
            }
        });

        topoCompleto.add(painelTitulo);
        topoCompleto.add(Box.createRigidArea(new Dimension(0, 22)));
        topoCompleto.add(painelProdutosBotao);

        return topoCompleto;
    }

    private void carregarProdutos() {
        List<Produto> produtos = produtoRepository.listar();
        produtosPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int colunas = 4;

        for (int i = 0; i < produtos.size(); i++) {
            Produto produto = produtos.get(i);
            JPanel card = criarCardProduto(produto);

            gbc.gridx = i % colunas;
            gbc.gridy = i / colunas;
            produtosPanel.add(card, gbc);
        }

        produtosPanel.revalidate();
        produtosPanel.repaint();
    }


    private JPanel criarCardProduto(Produto produto) {
        RoundedPanel card = new RoundedPanel(20);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(250, 280));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel imageLabel;
        try {
            ImageIcon icon = new ImageIcon(produto.getCaminhoImagem());
            Image scaled = icon.getImage().getScaledInstance(220, 130, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            imageLabel = new JLabel("Imagem não disponível");
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        card.add(imageLabel);

        card.add(Box.createVerticalStrut(15)); // novo espaçamento entre imagem e descrição

        JLabel nomeLabel = new JLabel("<html><div style='text-align: left; width: 200px;'>" + produto.getDescricao() + "</div></html>");
        nomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel precoLabel = new JLabel("R$ " + produto.getPreco());
        precoLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        precoLabel.setForeground(new Color(30, 30, 30));
        precoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton adicionarBtn = new RoundedButton("Adicionar ao carrinho", 20);
        adicionarBtn.setBackground(new Color(108, 99, 255));
        adicionarBtn.setForeground(Color.WHITE);
        adicionarBtn.setFocusPainted(false);
        adicionarBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        adicionarBtn.addActionListener(e -> {
            carrinho.adicionarProduto(produto, 1);
            JOptionPane.showMessageDialog(this, produto.getDescricao() + " adicionado ao carrinho!");
        });

        card.add(nomeLabel);
        card.add(Box.createVerticalGlue());
        card.add(precoLabel);
        card.add(adicionarBtn);
        card.add(Box.createVerticalStrut(10));

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
        public Insets getInsets() {
            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - shadowSize;
            int height = getHeight() - shadowSize;

            RoundRectangle2D.Float shadow = new RoundRectangle2D.Float(
                    shadowSize, shadowSize, width, height, radius, radius
            );
            g2.setColor(shadowColor);
            g2.fill(shadow);

            RoundRectangle2D.Float background = new RoundRectangle2D.Float(
                    0, 0, width, height, radius, radius
            );
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
            setForeground(Color.WHITE);
            setBackground(new Color(108, 99, 255));
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
            // Sem borda
        }
    }
}
