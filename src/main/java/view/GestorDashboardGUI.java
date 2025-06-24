package main.java.view;

import main.java.model.Venda;
import main.java.model.Usuario;
import main.java.model.Gestor;
import main.java.repository.VendaRepository;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class GestorDashboardGUI extends JFrame {

    private final Color CONTENT_BG = new Color(240, 242, 245);
    private final VendaRepository vendaRepository;
    private final Usuario usuarioAtual;
    
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private boolean isMaximized = false;
    private Rectangle windowBoundsBeforeMaximize;

    private java.util.List<JButton> sidebarButtons = new java.util.ArrayList<>();
    private String selectedSidebar = "INICIO";

    private JPanel summaryPanel;

    public GestorDashboardGUI(Usuario usuarioAtual) {
        // Verificar se o usuário é um Gestor
        if (!(usuarioAtual instanceof Gestor)) {
            JOptionPane.showMessageDialog(null, 
                "Acesso negado! Apenas gestores podem acessar esta área.", 
                "Erro de Acesso", 
                JOptionPane.ERROR_MESSAGE);
            throw new SecurityException("Acesso negado: usuário não é um Gestor");
        }
        
        this.usuarioAtual = usuarioAtual;
        this.vendaRepository = new VendaRepository();
        setTitle("Backoffice");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        contentPanel.add(new VendasGUI(), "VENDAS");
        contentPanel.add(new ProdutoGUI(), "PRODUTOS");
        contentPanel.add(new ClienteGUI(), "CLIENTES");
        contentPanel.add(new ConfiguracoesGUI(usuarioAtual), "CONFIGURACOES");
        
        contentPane.add(contentPanel, BorderLayout.CENTER);

        loadDashboardData();
    }

    private JPanel createSidebar() {
        Color azulEscuro = new Color(10, 10, 60);
        Color roxoTopo = new Color(108, 99, 255);
        Color roxoSelecionado = new Color(120, 100, 255);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(azulEscuro);
        sidebar.setPreferredSize(new Dimension(150, getHeight()));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(roxoTopo);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        sidebar.add(topPanel);

        sidebar.add(Box.createVerticalStrut(20));

        sidebar.add(createSidebarButton("Início", "INICIO", roxoSelecionado, azulEscuro));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createSidebarButton("Vendas", "VENDAS", roxoSelecionado, azulEscuro));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createSidebarButton("Produtos", "PRODUTOS", roxoSelecionado, azulEscuro));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createSidebarButton("Clientes", "CLIENTES", roxoSelecionado, azulEscuro));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createSidebarButton("Configurações", "CONFIGURACOES", roxoSelecionado, azulEscuro));

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

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

    @SuppressWarnings("unused")
    private void updateSidebarSelection(String text) {
        throw new UnsupportedOperationException("Unimplemented method 'updateSidebarSelection'");
    }

    private JPanel createHomePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(240, 242, 245));

        mainPanel.add(Box.createVerticalStrut(80));

        JLabel welcomeLabel = new JLabel("Olá, bem-vindo!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Exibir CPF do gestor
        String cpf = "";
        if (usuarioAtual instanceof Gestor) {
            cpf = ((Gestor) usuarioAtual).getCpf();
        }
        JLabel cpfLabel = new JLabel("CPF: " + cpf);
        cpfLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        cpfLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cpfLabel.setForeground(new Color(100, 116, 139));

        summaryPanel = createSummaryPanel();
        summaryPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel actionsPanel = createActionsPanel();
        actionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(cpfLabel);
        mainPanel.add(Box.createVerticalStrut(70));
        mainPanel.add(summaryPanel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(actionsPanel);

        return mainPanel;
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        summaryPanel.setBackground(new Color(240, 242, 245));
        summaryPanel.setBorder(new EmptyBorder(0, 24, 0, 24));

        summaryPanel.add(createSummaryCard("Faturamento", "R$ 0,00", new Color(111, 66, 193), "/images/icon_money.png"));
        summaryPanel.add(createSummaryCard("Receita líquida", "R$ 0,00", new Color(214, 51, 132), "/images/icon_receita.png"));
        summaryPanel.add(createSummaryCard("Total de vendas", "0", new Color(32, 201, 151), "/images/icon_cart.png"));
        summaryPanel.add(createSummaryCard("Ticket médio", "R$ 0,00", new Color(230, 86, 86), "/images/icon_ticket.png"));

        return summaryPanel;
    }

    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        actionsPanel.setBackground(new Color(240, 242, 245));
        actionsPanel.setBorder(new EmptyBorder(0, 24, 0, 24));

        actionsPanel.add(createActionButton("Nova Venda", "/images/action_new_sale.png", () -> new NovaVendaGUI(this).setVisible(true)));
        actionsPanel.add(createActionButton("Novo Produto", "/images/action_new_product.png", () -> new AdicionarProdutoModal(this).setVisible(true)));
        actionsPanel.add(createActionButton("Novo Cliente", "/images/action_new_client.png", () -> new AdicionarClienteModal(this).setVisible(true)));
        actionsPanel.add(createActionButton("Buscar Venda", "/images/action_search_sale.png", () -> showPanel("VENDAS")));
        
        return actionsPanel;
    }
    
    private JPanel createSummaryCard(String title, String value, Color accentColor, String iconPath) {
        JPanel card = new JPanel(new BorderLayout(10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,30));
                g2.fillRoundRect(4, 4, getWidth()-8, getHeight()-8, 24, 24);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 24, 24);
            }
        };
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 6, 0, 0, accentColor),
            new EmptyBorder(14, 14, 14, 14)
        ));
        card.setPreferredSize(new Dimension(220, 150));

        ImageIcon icon = new ImageIcon("src/main/resources" + iconPath);
        Image scaled = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaled));
        iconLabel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(new Color(120, 120, 120));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        valueLabel.setForeground(accentColor);
        valueLabel.setName("valueLabel");

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 247, 255));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return card;
    }

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

        ImageIcon icon = new ImageIcon("src/main/resources" + iconPath);
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

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }
    
    @SuppressWarnings("unused")
    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CONTENT_BG);
        JLabel label = new JLabel(text + " (Em construção)");
        label.setFont(new Font("SansSerif", Font.BOLD, 24));
        label.setForeground(Color.GRAY);
        panel.add(label);
        return panel;
    }

    private void loadDashboardData() {
        List<Venda> vendas = vendaRepository.listar();
        
        double faturamento = vendas.stream().mapToDouble(Venda::getTotal).sum();
        int totalVendas = vendas.size();
        double ticketMedio = (totalVendas > 0) ? faturamento / totalVendas : 0;
        
        double receitaLiquida = faturamento;
        
        @SuppressWarnings("deprecation")
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        currencyFormat.setMaximumFractionDigits(2);
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumIntegerDigits(10);

        @SuppressWarnings("unused")
        JPanel homePanel = (JPanel) contentPanel.getComponent(0);
        updateSummaryCard(summaryPanel, 0, currencyFormat.format(faturamento));
        updateSummaryCard(summaryPanel, 1, currencyFormat.format(receitaLiquida));
        updateSummaryCard(summaryPanel, 2, String.valueOf(totalVendas));
        updateSummaryCard(summaryPanel, 3, currencyFormat.format(ticketMedio));
    }

    private void updateSummaryCard(JPanel summaryPanel, int cardIndex, String value) {
        if (summaryPanel.getComponentCount() > cardIndex) {
            JPanel card = (JPanel) summaryPanel.getComponent(cardIndex);
            for (Component comp : card.getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component subComp : ((JPanel) comp).getComponents()) {
                        if (subComp instanceof JLabel && "valueLabel".equals(subComp.getName())) {
                            ((JLabel) subComp).setText(value);
                            return;
                        }
                    }
                }
                if (comp instanceof JLabel && "valueLabel".equals(comp.getName())) {
                    ((JLabel) comp).setText(value);
                    return;
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private JPanel createCustomTitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setLayout(new BorderLayout());
        titleBar.setBackground(new Color(108, 99, 255));
        titleBar.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel title = new JLabel("  Sistema de Gestão de Loja de Roupas");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleBar.add(title, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttons.setOpaque(false);

        JButton btnMin = new JButton(new ImageIcon("src/main/resources/images/minimize.png"));
        btnMin.setToolTipText("Minimizar");
        btnMin.setFocusPainted(false);
        btnMin.setBorderPainted(false);
        btnMin.setContentAreaFilled(false);
        btnMin.setPreferredSize(new Dimension(60, 60));
        btnMin.addActionListener(e -> setState(JFrame.ICONIFIED));
        buttons.add(btnMin);

        JButton btnMax = new JButton(new ImageIcon("src/main/resources/images/maximize.png"));
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
                btnMax.setIcon(new ImageIcon("src/main/resources/images/restore.png"));
            } else {
                setBounds(windowBoundsBeforeMaximize);
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                isMaximized = false;
                btnMax.setIcon(new ImageIcon("src/main/resources/images/maximize.png"));
            }
        });
        buttons.add(btnMax);

        JButton btnClose = new JButton(new ImageIcon("src/main/resources/images/close.png"));
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


}