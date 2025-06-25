package main.java.view;

import main.java.model.Carrinho;
import main.java.model.Produto;
import main.java.model.Usuario;
import main.java.model.Cliente;
import main.java.model.Venda;
import main.java.model.ItemVenda;
import main.java.repository.ProdutoRepository;
import main.java.repository.VendaRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Tela de dashboard do Cliente.
 * Permite ao cliente visualizar produtos, carrinho, histórico e configurações.
 *
 * @author Rafael
 */
public class ClienteDashboardGUI extends JFrame {
    private JPanel produtosPanel;
    private JPanel carrinhoPanel;
    private JPanel historicoPanel;
    private ProdutoRepository produtoRepository;
    private Carrinho carrinho;
    private Usuario usuarioAtual;
    private VendaRepository vendaRepository;
    
    // Cores do GestorDashboardGUI
    private static final Color SIDEBAR_BG = new Color(23, 34, 58);
    private static final Color CONTENT_BG = new Color(240, 242, 245);
    @SuppressWarnings("unused")
    private static final Color CARD_BG = Color.WHITE;
    private static final Color ROXO_TOPO = new Color(108, 99, 255);
    private static final Color ROXO_SELECIONADO = new Color(120, 100, 255);
    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private boolean isMaximized = false;
    private Rectangle windowBoundsBeforeMaximize;
    private final java.util.List<JButton> sidebarButtons = new java.util.ArrayList<>();
    private String selectedSidebar = "INICIO";

    /**
     * Construtor principal do dashboard do cliente.
     * @param usuarioAtual usuário logado (deve ser Cliente)
     */
    public ClienteDashboardGUI(Usuario usuarioAtual) {
        this(usuarioAtual, new ProdutoRepository(), new VendaRepository());
    }

    /**
     * Construtor do dashboard do cliente com repositórios customizados.
     * @param usuarioAtual usuário logado (deve ser Cliente)
     * @param produtoRepository repositório de produtos
     * @param vendaRepository repositório de vendas
     */
    public ClienteDashboardGUI(Usuario usuarioAtual, ProdutoRepository produtoRepository, VendaRepository vendaRepository) {
        // Verificar se o usuário é um Cliente
        if (!(usuarioAtual instanceof Cliente)) {
            JOptionPane.showMessageDialog(null, 
                "Acesso negado! Apenas clientes podem acessar esta área.", 
                "Erro de Acesso", 
                JOptionPane.ERROR_MESSAGE);
            throw new SecurityException("Acesso negado: usuário não é um Cliente");
        }
        
        this.usuarioAtual = usuarioAtual;
        this.produtoRepository = produtoRepository;
        this.vendaRepository = vendaRepository;
        carrinho = new Carrinho();

        setTitle("Loja - Cliente");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(createCustomTitleBar(), BorderLayout.NORTH);
        contentPane.add(createSidebar(), BorderLayout.WEST);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(CONTENT_BG);
        
        contentPanel.add(createHomePanel(), "INICIO");
        contentPanel.add(createProdutosPanel(), "PRODUTOS");
        contentPanel.add(createCarrinhoPanel(), "CARRINHO");
        historicoPanel = createHistoricoPanel();
        contentPanel.add(historicoPanel, "HISTORICO");
        contentPanel.add(createConfiguracoesPanel(), "CONFIGURACOES");
        
        contentPane.add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Construtor sem parâmetros (acesso negado).
     */
    public ClienteDashboardGUI() {
        JOptionPane.showMessageDialog(null, 
            "Acesso negado! É necessário fazer login como cliente.", 
            "Erro de Acesso", 
            JOptionPane.ERROR_MESSAGE);
        throw new SecurityException("Acesso negado: login necessário");
    }

    /**
     * Cria a barra lateral de navegação.
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(150, getHeight()));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(ROXO_TOPO);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        sidebar.add(topPanel);

        sidebar.add(Box.createVerticalStrut(20));

        sidebar.add(createSidebarButton("Início", "INICIO", ROXO_SELECIONADO, SIDEBAR_BG));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createSidebarButton("Produtos", "PRODUTOS", ROXO_SELECIONADO, SIDEBAR_BG));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createSidebarButton("Carrinho", "CARRINHO", ROXO_SELECIONADO, SIDEBAR_BG));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createSidebarButton("Histórico", "HISTORICO", ROXO_SELECIONADO, SIDEBAR_BG));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createSidebarButton("Configurações", "CONFIGURACOES", ROXO_SELECIONADO, SIDEBAR_BG));

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    /**
     * Cria um botão da barra lateral.
     */
    @SuppressWarnings("unused")
    private JButton createSidebarButton(String text, String cardName, Color selectedColor, Color bgColor) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(selectedSidebar.equals(cardName) ? selectedColor : bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setPreferredSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setMinimumSize(new Dimension(0, 44));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.putClientProperty("cardName", cardName);
        sidebarButtons.add(button);

        button.addActionListener(e -> {
            selectedSidebar = cardName;
            for (JButton btn : sidebarButtons) {
                String btnCard = (String) btn.getClientProperty("cardName");
                btn.setBackground(selectedSidebar.equals(btnCard) ? selectedColor : bgColor);
            }
            showPanel(cardName);
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!selectedSidebar.equals(cardName))
                    button.setBackground(selectedColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(selectedSidebar.equals(cardName) ? selectedColor : bgColor);
            }
        });

        return button;
    }

    /**
     * Cria o painel inicial (home) do dashboard.
     */
    private JPanel createHomePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CONTENT_BG);

        mainPanel.add(Box.createVerticalStrut(80));

        JLabel welcomeLabel = new JLabel("Bem-vindo, " + usuarioAtual.getNome() + "!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Explore nossos produtos e faça suas compras");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel actionsPanel = createActionsPanel();
        actionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(80));
        mainPanel.add(actionsPanel);

        return mainPanel;
    }

    /**
     * Cria o painel de ações rápidas.
     */
    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        actionsPanel.setBackground(CONTENT_BG);
        actionsPanel.setBorder(new EmptyBorder(0, 24, 0, 24));

        actionsPanel.add(createActionButton("Ver Produtos", "/images/action_new_product.png", () -> showPanel("PRODUTOS")));
        actionsPanel.add(createActionButton("Meu Carrinho", "/images/action_new_sale.png", () -> showPanel("CARRINHO")));
        actionsPanel.add(createActionButton("Histórico", "/images/action_search_sale.png", () -> {
            atualizarHistorico();
            showPanel("HISTORICO");
        }));
        
        return actionsPanel;
    }

    /**
     * Cria um botão de ação rápida.
     */
    private JPanel createActionButton(String text, String iconPath, Runnable action) {
        JPanel panel = new JPanel(new BorderLayout(0,10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20,20,20,20));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setPreferredSize(new Dimension(200, 200));

        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaled));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(textLabel, BorderLayout.SOUTH);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.run();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(245, 247, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.WHITE);
            }
        });

        return panel;
    }

    private JPanel createProdutosPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(CONTENT_BG);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Produtos Disponíveis");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Produtos
        produtosPanel = new JPanel();
        produtosPanel.setLayout(new GridBagLayout());
        produtosPanel.setBackground(CONTENT_BG);
        produtosPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JScrollPane scrollPane = new JScrollPane(produtosPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CONTENT_BG);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        carregarProdutos();
        return mainPanel;
    }

    private JPanel createCarrinhoPanel() {
        carrinhoPanel = new JPanel(new BorderLayout());
        carrinhoPanel.setBackground(CONTENT_BG);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("🛒 Meu Carrinho");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        carrinhoPanel.add(headerPanel, BorderLayout.NORTH);

        // Conteúdo inicial do carrinho
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setBackground(CONTENT_BG);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        if (carrinho.getItens().isEmpty()) {
            JLabel emptyLabel = new JLabel("🛒 Seu carrinho está vazio");
            emptyLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            emptyLabel.setForeground(new Color(148, 163, 184));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            // Se houver itens, atualizar o painel
            atualizarCarrinhoPanel();
            return carrinhoPanel;
        }

        carrinhoPanel.add(contentPanel, BorderLayout.CENTER);
        return carrinhoPanel;
    }

    @SuppressWarnings("unused")
    private JPanel createHistoricoPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(CONTENT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titleLabel = new JLabel("📋 Histórico de Compras");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 41, 59));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Buscar vendas do cliente
        java.util.List<Venda> vendas = vendaRepository.listarPorCliente((Cliente) usuarioAtual);

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setOpaque(false);

        if (vendas.isEmpty()) {
            JLabel emptyLabel = new JLabel("📭 Nenhuma compra encontrada");
            emptyLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            emptyLabel.setForeground(new Color(148, 163, 184));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cardsPanel.add(emptyLabel);
        } else {
            for (Venda venda : vendas) {
                JPanel card = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        GradientPaint gradient = new GradientPaint(
                            0, 0, new Color(255, 255, 255),
                            0, getHeight(), new Color(248, 250, 252)
                        );
                        g2.setPaint(gradient);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.setColor(new Color(226, 232, 240));
                        g2.setStroke(new BasicStroke(1.5f));
                        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                    }
                };
                card.setLayout(new BorderLayout(20, 0));
                card.setOpaque(false);
                card.setPreferredSize(new Dimension(0, 110));
                card.setMaximumSize(new Dimension(900, 110));
                card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

                // Info principal
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setOpaque(false);
                JLabel dataLabel = new JLabel("Data: " + venda.getData().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                dataLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                dataLabel.setForeground(new Color(100, 116, 139));
                JLabel totalLabel = new JLabel("Total: R$ " + String.format("%.2f", venda.getTotal()));
                totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
                totalLabel.setForeground(ROXO_TOPO);
                JLabel itensLabel = new JLabel("Itens: " + venda.getItens().size());
                itensLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                itensLabel.setForeground(new Color(100, 116, 139));
                infoPanel.add(dataLabel);
                infoPanel.add(Box.createVerticalStrut(4));
                infoPanel.add(totalLabel);
                infoPanel.add(Box.createVerticalStrut(4));
                infoPanel.add(itensLabel);

                // Botão detalhes
                JButton detalhesBtn = new JButton("Ver detalhes") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        GradientPaint buttonGradient = new GradientPaint(
                            0, 0, ROXO_TOPO,
                            0, getHeight(), new Color(99, 102, 241)
                        );
                        g2.setPaint(buttonGradient);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                        FontMetrics fm = g2.getFontMetrics();
                        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                        int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                        g2.drawString(getText(), textX, textY);
                        g2.dispose();
                    }
                };
                detalhesBtn.setPreferredSize(new Dimension(140, 38));
                detalhesBtn.setMaximumSize(new Dimension(140, 38));
                detalhesBtn.setFocusPainted(false);
                detalhesBtn.setBorderPainted(false);
                detalhesBtn.setContentAreaFilled(false);
                detalhesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                detalhesBtn.addActionListener(e -> mostrarDetalhesVenda(venda));

                card.add(infoPanel, BorderLayout.CENTER);
                card.add(detalhesBtn, BorderLayout.EAST);
                cardsPanel.add(card);
                cardsPanel.add(Box.createVerticalStrut(18));
            }
        }

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private void mostrarDetalhesVenda(Venda venda) {
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("📋 Detalhes da Compra\n\n");
        detalhes.append("Data: ").append(venda.getData().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        detalhes.append("Total: R$ ").append(String.format("%.2f", venda.getTotal())).append("\n");
        detalhes.append("Itens: ").append(venda.getItens().size()).append("\n");
        if (venda.getFormaPagamento() != null && !venda.getFormaPagamento().isEmpty()) {
            detalhes.append("Pagamento: ").append(venda.getFormaPagamento()).append("\n");
        }
        if (venda.getEndereco() != null && !venda.getEndereco().isEmpty()) {
            detalhes.append("Endereço: ").append(venda.getEndereco()).append("\n");
        }
        detalhes.append("\n📦 Produtos:\n");

        for (ItemVenda item : venda.getItens()) {
            detalhes.append("• ").append(item.getProduto().getDescricao())
                   .append(" - ").append(item.getQuantidade()).append("x")
                   .append(" R$ ").append(String.format("%.2f", item.getProduto().getPreco()))
                   .append(" = R$ ").append(String.format("%.2f", item.getQuantidade() * item.getProduto().getPreco()))
                   .append("\n");
        }

        JOptionPane.showMessageDialog(this,
            detalhes.toString(),
            "Detalhes da Compra",
            JOptionPane.INFORMATION_MESSAGE);
    }

    @SuppressWarnings("unused")
    private JPanel criarItemCarrinho(main.java.model.CarrinhoItem item) {
        JPanel itemPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradiente igual ao dos cards de produto
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255),
                    0, getHeight(), new Color(248, 250, 252)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // Borda igual ao dos cards de produto
                g2.setColor(new Color(226, 232, 240));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
        };
        itemPanel.setLayout(new BorderLayout(20, 0));
        itemPanel.setOpaque(false);
        itemPanel.setPreferredSize(new Dimension(320, 140));
        itemPanel.setMaximumSize(new Dimension(600, 140));
        itemPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Imagem do produto
        JPanel imageContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint imageGradient = new GradientPaint(
                    0, 0, new Color(241, 245, 249),
                    0, getHeight(), new Color(226, 232, 240)
                );
                g2.setPaint(imageGradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(90, 90));
        imageContainer.setMaximumSize(new Dimension(90, 90));
        imageContainer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel imageLabel;
        try {
            ImageIcon icon = new ImageIcon(item.getProduto().getCaminhoImagem());
            Image scaled = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaled));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            imageLabel = new JLabel("📦");
            imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 32));
            imageLabel.setForeground(new Color(148, 163, 184));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        imageContainer.add(imageLabel, BorderLayout.CENTER);

        // Informações do produto
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nomeLabel = new JLabel(item.getProduto().getDescricao());
        nomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nomeLabel.setForeground(new Color(30, 41, 59));

        JLabel categoriaLabel = new JLabel(item.getProduto().getCategoria().getNome() + " • " + item.getProduto().getMarca().getNome());
        categoriaLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        categoriaLabel.setForeground(new Color(100, 116, 139));

        JLabel precoLabel = new JLabel("R$ " + String.format("%.2f", item.getProduto().getPreco()) + " x " + item.getQuantidade());
        precoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        precoLabel.setForeground(new Color(100, 116, 139));

        JLabel subtotalLabel = new JLabel("Subtotal: R$ " + String.format("%.2f", item.getSubtotal()));
        subtotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        subtotalLabel.setForeground(ROXO_TOPO);

        infoPanel.add(nomeLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(categoriaLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(precoLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(subtotalLabel);

        // Painel dos botões
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new BoxLayout(botoesPanel, BoxLayout.Y_AXIS));
        botoesPanel.setOpaque(false);

        JButton removerBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint buttonGradient = new GradientPaint(
                    0, 0, new Color(239, 68, 68),
                    0, getHeight(), new Color(220, 38, 38)
                );
                g2.setPaint(buttonGradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        removerBtn.setText("🗑️ Remover");
        removerBtn.setPreferredSize(new Dimension(120, 35));
        removerBtn.setMaximumSize(new Dimension(120, 35));
        removerBtn.setFocusPainted(false);
        removerBtn.setBorderPainted(false);
        removerBtn.setContentAreaFilled(false);
        removerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removerBtn.addActionListener(e -> {
            carrinho.removerProduto(item.getProduto());
            atualizarCarrinhoPanel();
        });

        botoesPanel.add(removerBtn);
        botoesPanel.add(Box.createVerticalStrut(8));

        JButton aumentarBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint buttonGradient = new GradientPaint(
                    0, 0, new Color(34, 197, 94),
                    0, getHeight(), new Color(22, 163, 74)
                );
                g2.setPaint(buttonGradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        aumentarBtn.setText("➕ +1");
        aumentarBtn.setPreferredSize(new Dimension(120, 35));
        aumentarBtn.setMaximumSize(new Dimension(120, 35));
        aumentarBtn.setFocusPainted(false);
        aumentarBtn.setBorderPainted(false);
        aumentarBtn.setContentAreaFilled(false);
        aumentarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aumentarBtn.addActionListener(e -> {
            carrinho.adicionarProduto(item.getProduto(), 1);
            atualizarCarrinhoPanel();
        });

        botoesPanel.add(aumentarBtn);

        itemPanel.add(imageContainer, BorderLayout.WEST);
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(botoesPanel, BorderLayout.EAST);

        return itemPanel;
    }

    @SuppressWarnings("unused")
    private void atualizarCarrinhoPanel() {
        carrinhoPanel.removeAll();
        carrinhoPanel.setLayout(new BorderLayout(0, 0));

        // Painel dos itens (CENTRO)
        JPanel itensPanel = new JPanel();
        itensPanel.setLayout(new BoxLayout(itensPanel, BoxLayout.Y_AXIS));
        itensPanel.setOpaque(false);
        itensPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        if (carrinho.getItens().isEmpty()) {
            JLabel emptyLabel = new JLabel("🛒 Seu carrinho está vazio");
            emptyLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            emptyLabel.setForeground(new Color(148, 163, 184));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itensPanel.add(emptyLabel);
        } else {
            for (main.java.model.CarrinhoItem item : carrinho.getItens()) {
                itensPanel.add(criarItemCarrinho(item));
                itensPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(itensPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Painel de resumo (EMBAIXO)
        JPanel resumoPanel = new JPanel();
        resumoPanel.setLayout(new BoxLayout(resumoPanel, BoxLayout.X_AXIS));
        resumoPanel.setOpaque(false);
        resumoPanel.setPreferredSize(new Dimension(0, 110));
        resumoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        resumoPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Gradiente e borda
        resumoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255),
                    0, getHeight(), new Color(248, 250, 252)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(226, 232, 240));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
        };
        resumoPanel.setLayout(new BoxLayout(resumoPanel, BoxLayout.X_AXIS));
        resumoPanel.setOpaque(false);
        resumoPanel.setPreferredSize(new Dimension(0, 110));
        resumoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        resumoPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Informações do resumo
        JLabel itensLabel = new JLabel("Itens: " + carrinho.getItens().size());
        itensLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        itensLabel.setForeground(new Color(100, 116, 139));
        JLabel totalLabel = new JLabel("Total: R$ " + String.format("%.2f", carrinho.getTotal()));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        totalLabel.setForeground(ROXO_TOPO);

        // Espaço flexível
        resumoPanel.add(itensLabel);
        resumoPanel.add(Box.createHorizontalStrut(30));
        resumoPanel.add(totalLabel);
        resumoPanel.add(Box.createHorizontalGlue());

        // Botão limpar
        JButton limparBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint buttonGradient = new GradientPaint(
                    0, 0, new Color(239, 68, 68),
                    0, getHeight(), new Color(220, 38, 38)
                );
                g2.setPaint(buttonGradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        limparBtn.setText("🗑️ Limpar Carrinho");
        limparBtn.setPreferredSize(new Dimension(180, 45));
        limparBtn.setMaximumSize(new Dimension(180, 45));
        limparBtn.setFocusPainted(false);
        limparBtn.setBorderPainted(false);
        limparBtn.setContentAreaFilled(false);
        limparBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        limparBtn.addActionListener(e -> {
            carrinho.limpar();
            atualizarCarrinhoPanel();
        });
        limparBtn.setEnabled(!carrinho.getItens().isEmpty());

        // Botão finalizar
        JButton finalizarBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint buttonGradient = new GradientPaint(
                    0, 0, ROXO_TOPO,
                    0, getHeight(), new Color(99, 102, 241)
                );
                g2.setPaint(buttonGradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        finalizarBtn.setText("💳 Finalizar Compra");
        finalizarBtn.setPreferredSize(new Dimension(180, 45));
        finalizarBtn.setMaximumSize(new Dimension(180, 45));
        finalizarBtn.setFocusPainted(false);
        finalizarBtn.setBorderPainted(false);
        finalizarBtn.setContentAreaFilled(false);
        finalizarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        finalizarBtn.addActionListener(e -> {
            if (!carrinho.getItens().isEmpty()) {
                FinalizarCompraWizard wizard = new FinalizarCompraWizard(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    carrinho.getTotal(),
                    dados -> {
                        String[] partes = dados.split("\\|");
                        if (partes.length == 2) {
                            finalizarCompra(partes[0], partes[1]);
                        } else {
                            finalizarCompra("", "");
                        }
                    }
                );
                wizard.setVisible(true);
            }
        });
        finalizarBtn.setEnabled(!carrinho.getItens().isEmpty());

        resumoPanel.add(limparBtn);
        resumoPanel.add(Box.createHorizontalStrut(15));
        resumoPanel.add(finalizarBtn);

        carrinhoPanel.add(scrollPane, BorderLayout.CENTER);
        carrinhoPanel.add(resumoPanel, BorderLayout.SOUTH);
        carrinhoPanel.revalidate();
        carrinhoPanel.repaint();
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

    @SuppressWarnings("unused")
    private JPanel criarCardProduto(Produto produto) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Criar gradiente de fundo
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255),
                    0, getHeight(), new Color(248, 250, 252)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Adicionar borda sutil
                g2.setColor(new Color(226, 232, 240));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
        };
        card.setLayout(new BorderLayout(0, 0));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(320, 420));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Painel principal do card
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);

        // Container da imagem com fundo gradiente
        JPanel imageContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente para o container da imagem
                GradientPaint imageGradient = new GradientPaint(
                    0, 0, new Color(241, 245, 249),
                    0, getHeight(), new Color(226, 232, 240)
                );
                g2.setPaint(imageGradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        imageContainer.setLayout(new BorderLayout());
        imageContainer.setPreferredSize(new Dimension(290, 200));
        imageContainer.setMaximumSize(new Dimension(290, 200));
        imageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Imagem do produto
        JLabel imageLabel;
        try {
            ImageIcon icon = new ImageIcon(produto.getCaminhoImagem());
            Image scaled = icon.getImage().getScaledInstance(270, 180, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaled));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            imageLabel = new JLabel("📦");
            imageLabel.setFont(new Font("SansSerif", Font.PLAIN, 60));
            imageLabel.setForeground(new Color(148, 163, 184));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        imageContainer.add(imageLabel, BorderLayout.CENTER);

        mainContent.add(imageContainer);
        mainContent.add(Box.createVerticalStrut(20));

        // Informações do produto
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Nome do produto
        JLabel nomeLabel = new JLabel("<html><div style='text-align: center; width: 270px; color: #1e293b;'>" + 
            produto.getDescricao() + "</div></html>");
        nomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nomeLabel.setForeground(new Color(30, 41, 59));

        // Categoria e marca
        JLabel categoriaLabel = new JLabel(produto.getCategoria().getNome() + " • " + produto.getMarca().getNome());
        categoriaLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        categoriaLabel.setForeground(new Color(100, 116, 139));
        categoriaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Preço
        JLabel precoLabel = new JLabel("R$ " + String.format("%.2f", produto.getPreco()));
        precoLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        precoLabel.setForeground(ROXO_TOPO);
        precoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Estoque
        JLabel estoqueLabel = new JLabel("Estoque: " + produto.getQtd() + " unidades");
        estoqueLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        estoqueLabel.setForeground(new Color(148, 163, 184));
        estoqueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(nomeLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(categoriaLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(precoLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(estoqueLabel);

        mainContent.add(infoPanel);
        mainContent.add(Box.createVerticalStrut(20));

        // Botão de adicionar ao carrinho
        JButton adicionarBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente do botão
                GradientPaint buttonGradient = new GradientPaint(
                    0, 0, ROXO_TOPO,
                    0, getHeight(), new Color(99, 102, 241)
                );
                g2.setPaint(buttonGradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Texto do botão
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };
        adicionarBtn.setText("🛒 Adicionar ao Carrinho");
        adicionarBtn.setPreferredSize(new Dimension(290, 50));
        adicionarBtn.setMaximumSize(new Dimension(290, 50));
        adicionarBtn.setFocusPainted(false);
        adicionarBtn.setBorderPainted(false);
        adicionarBtn.setContentAreaFilled(false);
        adicionarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adicionarBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        adicionarBtn.addActionListener(e -> {
            carrinho.adicionarProduto(produto, 1);
            JOptionPane.showMessageDialog(this, 
                "✅ " + produto.getDescricao() + " adicionado ao carrinho!", 
                "Produto Adicionado", 
                JOptionPane.INFORMATION_MESSAGE);
            atualizarCarrinhoPanel();
        });

        // Efeito hover no botão
        adicionarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                adicionarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                adicionarBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        mainContent.add(adicionarBtn);

        card.add(mainContent, BorderLayout.CENTER);

        return card;
    }

    private void showPanel(String panelName) {
        // Se estiver navegando para o histórico, atualizar os dados
        if (panelName.equals("HISTORICO")) {
            atualizarHistorico();
        }
        
        cardLayout.show(contentPanel, panelName);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @SuppressWarnings("unused")
    private JPanel createCustomTitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setLayout(new BorderLayout());
        titleBar.setBackground(ROXO_TOPO);
        titleBar.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel title = new JLabel("  Loja Online - Área do Cliente");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleBar.add(title, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttons.setOpaque(false);

        JButton btnMin = new JButton(new ImageIcon(getClass().getResource("/images/minimize.png")));
        btnMin.setToolTipText("Minimizar");
        btnMin.setFocusPainted(false);
        btnMin.setBorderPainted(false);
        btnMin.setContentAreaFilled(false);
        btnMin.setPreferredSize(new Dimension(60, 60));
        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
        buttons.add(btnMin);

        JButton btnMax = new JButton(new ImageIcon(getClass().getResource("/images/maximize.png")));
        btnMax.setToolTipText("Maximizar/Restaurar");
        btnMax.setFocusPainted(false);
        btnMax.setBorderPainted(false);
        btnMax.setContentAreaFilled(false);
        btnMax.setPreferredSize(new Dimension(60, 60));
        btnMax.addActionListener(e -> {
            if (!isMaximized) {
                windowBoundsBeforeMaximize = getBounds();
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                Rectangle bounds = env.getMaximumWindowBounds();
                setBounds(bounds);
                setShape(null);
                isMaximized = true;
                btnMax.setIcon(new ImageIcon(getClass().getResource("/images/restore.png")));
            } else {
                setBounds(windowBoundsBeforeMaximize);
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                isMaximized = false;
                btnMax.setIcon(new ImageIcon(getClass().getResource("/images/maximize.png")));
            }
        });
        buttons.add(btnMax);

        JButton btnClose = new JButton(new ImageIcon(getClass().getResource("/images/close.png")));
        btnClose.setToolTipText("Fechar");
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setPreferredSize(new Dimension(60, 60));
        btnClose.addActionListener(e -> System.exit(0));
        buttons.add(btnClose);

        titleBar.add(buttons, BorderLayout.EAST);

        final Point[] mouseDownCompCoords = {null};
        titleBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                mouseDownCompCoords[0] = e.getPoint();
            }
        });
        titleBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords[0].x, currCoords.y - mouseDownCompCoords[0].y);
            }
        });

        return titleBar;
    }

    private void finalizarCompra(String dadosPagamento, String dadosEndereco) {
        if (carrinho.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "❌ Seu carrinho está vazio!", 
                "Carrinho Vazio", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
            "💳 Confirmar compra de R$ " + String.format("%.2f", carrinho.getTotal()) + "?",
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                java.util.List<main.java.model.ItemVenda> itensVenda = new java.util.ArrayList<>();
                for (main.java.model.CarrinhoItem item : carrinho.getItens()) {
                    main.java.model.ItemVenda itemVenda = new main.java.model.ItemVenda(
                        item.getProduto(),
                        item.getQuantidade()
                    );
                    itensVenda.add(itemVenda);
                    main.java.model.Produto produto = item.getProduto();
                    produto.setQtd(produto.getQtd() - item.getQuantidade());
                    produtoRepository.atualizar(produto);
                }
                main.java.model.Venda venda = new main.java.model.Venda((main.java.model.Cliente) usuarioAtual, itensVenda, dadosPagamento, dadosEndereco);
                venda.setTotal(carrinho.getTotal());
                venda.setData(java.time.LocalDateTime.now());
                vendaRepository.adicionar(venda);
                carrinho.limpar();
                carregarProdutos();
                atualizarCarrinhoPanel();
                
                // Atualizar dashboard do gestor em tempo real
                main.java.view.GestorDashboardGUI.atualizarDashboardSeAtivo();
                
                // Atualizar histórico do cliente em tempo real
                atualizarHistorico();
                
                // Atualizar seleção da sidebar para HISTORICO
                selectedSidebar = "HISTORICO";
                for (JButton btn : sidebarButtons) {
                    String btnCard = (String) btn.getClientProperty("cardName");
                    btn.setBackground(selectedSidebar.equals(btnCard) ? ROXO_SELECIONADO : SIDEBAR_BG);
                }
                
                showPanel("HISTORICO");
                
                JOptionPane.showMessageDialog(this,
                    "✅ Compra finalizada com sucesso!\n\n" +
                    "Total: R$ " + String.format("%.2f", venda.getTotal()) + "\n" +
                    "Itens: " + venda.getItens().size() + "\n" +
                    "Pagamento: " + (dadosPagamento.isEmpty() ? "Não informado" : dadosPagamento) + "\n" +
                    "Endereço: " + (dadosEndereco.isEmpty() ? "Não informado" : dadosEndereco) + "\n" +
                    "Data: " + venda.getData().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    "Compra Realizada",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Erro ao finalizar compra: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @SuppressWarnings("unused")
    private JPanel createConfiguracoesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CONTENT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Configurações");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CONTENT_BG);

        // Card de informações do usuário
        JPanel userInfo = new JPanel(new BorderLayout());
        userInfo.setBackground(Color.WHITE);
        userInfo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        JLabel sectionTitle = new JLabel("Informações do Usuário");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(51, 51, 51));
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        userInfo.add(sectionTitle, BorderLayout.NORTH);
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 15, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(new JLabel("Nome:"));
        infoPanel.add(new JLabel(usuarioAtual.getNome()));
        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(new JLabel(usuarioAtual.getEmail()));
        infoPanel.add(new JLabel("Tipo:"));
        infoPanel.add(new JLabel("Cliente"));
        userInfo.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(userInfo);
        mainPanel.add(Box.createVerticalStrut(30));

        // Card de opções do sistema
        JPanel systemOptions = new JPanel(new BorderLayout());
        systemOptions.setBackground(Color.WHITE);
        systemOptions.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        JLabel sectionTitle2 = new JLabel("Opções do Sistema");
        sectionTitle2.setFont(new Font("SansSerif", Font.BOLD, 18));
        sectionTitle2.setForeground(new Color(51, 51, 51));
        sectionTitle2.setBorder(new EmptyBorder(0, 0, 15, 0));
        systemOptions.add(sectionTitle2, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton btnAlterarSenha = new JButton("🔒 Alterar Senha");
        btnAlterarSenha.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAlterarSenha.setForeground(Color.WHITE);
        btnAlterarSenha.setBackground(new Color(32, 201, 151));
        btnAlterarSenha.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btnAlterarSenha.setFocusPainted(false);
        btnAlterarSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAlterarSenha.addActionListener(e -> ClienteDashboardGUI.this.alterarSenhaCliente());
        buttonPanel.add(btnAlterarSenha);
        JButton btnSair = new JButton("🚪 Sair do Sistema");
        btnSair.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSair.setForeground(Color.WHITE);
        btnSair.setBackground(new Color(230, 86, 86));
        btnSair.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btnSair.setFocusPainted(false);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSair.setBackground(new Color(180, 50, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSair.setBackground(new Color(230, 86, 86));
            }
        });
        btnSair.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(panel);
            if (window != null) {
                window.dispose();
            }
            new LoginGUI(new main.java.service.AutenticacaoService(new main.java.repository.UsuarioRepository())).setVisible(true);
        });
        buttonPanel.add(btnSair);
        systemOptions.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(systemOptions);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
        @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(108, 99, 255);
                this.trackColor = new Color(240, 240, 240);
            }
        });

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    @SuppressWarnings("unused")
    private void alterarSenhaCliente() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Alterar Senha", true);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(CONTENT_BG);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        JLabel titleLabel = new JLabel("Alterar Senha");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        JPasswordField campoSenhaAtual = new JPasswordField();
        campoSenhaAtual.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campoSenhaAtual.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoSenhaAtual.setBorder(BorderFactory.createTitledBorder("Senha Atual"));
        JPasswordField campoNovaSenha = new JPasswordField();
        campoNovaSenha.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campoNovaSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoNovaSenha.setBorder(BorderFactory.createTitledBorder("Nova Senha"));
        JPasswordField campoConfirmarSenha = new JPasswordField();
        campoConfirmarSenha.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campoConfirmarSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoConfirmarSenha.setBorder(BorderFactory.createTitledBorder("Confirmar Nova Senha"));
        mainPanel.add(campoSenhaAtual);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(campoNovaSenha);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(campoConfirmarSenha);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        JButton btnSalvar = new JButton("💾 Salvar");
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setBackground(new Color(32, 201, 151));
        btnSalvar.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(230, 86, 86));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> {
            String senhaAtual = new String(campoSenhaAtual.getPassword());
            String novaSenha = new String(campoNovaSenha.getPassword());
            String confirmarSenha = new String(campoConfirmarSenha.getPassword());
            if (!usuarioAtual.getSenha().equals(senhaAtual)) {
                JOptionPane.showMessageDialog(dialog, "Senha atual incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!novaSenha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(dialog, "As senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (novaSenha.length() < 4) {
                JOptionPane.showMessageDialog(dialog, "A nova senha deve ter pelo menos 4 caracteres!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            usuarioAtual.setSenha(novaSenha);
            JOptionPane.showMessageDialog(dialog, "Senha alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        btnCancelar.addActionListener(e -> dialog.dispose());
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        mainPanel.add(buttonPanel);
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Atualiza o painel de histórico com as vendas mais recentes.
     */
    private void atualizarHistorico() {
        if (historicoPanel != null) {
            // Remover o painel antigo
            contentPanel.remove(historicoPanel);
            
            // Criar novo painel com dados atualizados
            historicoPanel = createHistoricoPanel();
            contentPanel.add(historicoPanel, "HISTORICO");
            
            // Se estiver no painel de histórico, mostrar o novo
            if (selectedSidebar.equals("HISTORICO")) {
                cardLayout.show(contentPanel, "HISTORICO");
            }
            
            // Forçar revalidação e repintura
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }
}
