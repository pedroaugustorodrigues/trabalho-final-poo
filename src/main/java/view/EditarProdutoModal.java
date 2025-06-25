package main.java.view;

import main.java.model.Categoria;
import main.java.model.Marca;
import main.java.model.Produto;
import main.java.repository.CategoriaRepository;
import main.java.repository.MarcaRepository;
import main.java.repository.ProdutoRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Modal para edição dos dados de um produto existente.
 * Permite alterar descrição, categoria, marca, preço e quantidade.
 *
 * @author Claudio
 */
public class EditarProdutoModal extends JDialog {
    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(108, 99, 255);
    private static final Color ACCENT_COLOR = new Color(32, 201, 151);
    private static final Color DANGER_COLOR = new Color(230, 86, 86);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);

    private JTextField txtDescricao;
    private JComboBox<Categoria> cbCategoria;
    private JComboBox<Marca> cbMarca;
    private JTextField txtPreco;
    private JTextField txtQuantidade;
    private JButton btnAdicionarCategoria;
    private JButton btnAdicionarMarca;
    private JButton btnSalvar;
    private JButton btnCancelar;
    
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProdutoRepository produtoRepository;
    private final Produto produtoOriginal;
    
    private boolean produtoEditado = false;

    /**
     * Cria o modal de edição de produto.
     * @param parent janela pai
     * @param produto produto a ser editado
     */
    public EditarProdutoModal(Frame parent, Produto produto) {
        super(parent, "Editar Produto", true);
        this.produtoOriginal = produto;
        
        categoriaRepository = new CategoriaRepository();
        marcaRepository = new MarcaRepository();
        produtoRepository = new ProdutoRepository();
        
        initComponents();
        setupLayout();
        setupListeners();
        carregarDados();
        preencherDados();
        
        setSize(600, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    /**
     * Inicializa os componentes do formulário.
     */
    private void initComponents() {
        txtDescricao = createStyledTextField("", "Descrição do Produto");

        cbCategoria = new JComboBox<>();
        stylizeComboBox(cbCategoria);

        cbMarca = new JComboBox<>();
        stylizeComboBox(cbMarca);

        txtPreco = createStyledTextField("", "0,00");
        txtQuantidade = createStyledTextField("", "0");
        
        ImageIcon plusIcon = new ImageIcon(getClass().getResource("/main/resources/images/plus.png"));

        btnAdicionarCategoria = new JButton(plusIcon);
        btnAdicionarCategoria.setPreferredSize(new Dimension(35, 35));
        btnAdicionarCategoria.setBackground(PRIMARY_COLOR);
        btnAdicionarCategoria.setForeground(Color.WHITE);
        btnAdicionarCategoria.setBorderPainted(false);
        btnAdicionarCategoria.setFocusPainted(false);
        
        btnAdicionarMarca = new JButton(plusIcon);
        btnAdicionarMarca.setPreferredSize(new Dimension(35, 35));
        btnAdicionarMarca.setBackground(PRIMARY_COLOR);
        btnAdicionarMarca.setForeground(Color.WHITE);
        btnAdicionarMarca.setBorderPainted(false);
        btnAdicionarMarca.setFocusPainted(false);
        
        btnSalvar = new JButton("Salvar");
        btnSalvar.setPreferredSize(new Dimension(120, 40));
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSalvar.setBackground(ACCENT_COLOR);
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFocusPainted(false);
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(120, 40));
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancelar.setBackground(DANGER_COLOR);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
    }

    /**
     * Organiza o layout dos componentes na tela.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(CARD_BG);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        
        JLabel lblTitulo = new JLabel("Editar Produto");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(TEXT_COLOR);
        lblTitulo.setBorder(new EmptyBorder(0, 20, 0, 0));
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(CARD_BG);
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(createLabel("Descrição:"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        contentPanel.add(txtDescricao, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(createLabel("Categoria:"), gbc);
        
        JPanel categoriaPanel = new JPanel(new BorderLayout(5, 0));
        categoriaPanel.setOpaque(false);
        categoriaPanel.add(cbCategoria, BorderLayout.CENTER);
        categoriaPanel.add(btnAdicionarCategoria, BorderLayout.EAST);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        contentPanel.add(categoriaPanel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        contentPanel.add(createLabel("Marca:"), gbc);
        
        JPanel marcaPanel = new JPanel(new BorderLayout(5, 0));
        marcaPanel.setOpaque(false);
        marcaPanel.add(cbMarca, BorderLayout.CENTER);
        marcaPanel.add(btnAdicionarMarca, BorderLayout.EAST);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        contentPanel.add(marcaPanel, gbc);
        
        JPanel priceQtyPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        priceQtyPanel.setOpaque(false);
        priceQtyPanel.add(createLabel("Preço (R$):"));
        priceQtyPanel.add(txtPreco);
        priceQtyPanel.add(createLabel("Quantidade:"));
        priceQtyPanel.add(txtQuantidade);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        contentPanel.add(priceQtyPanel, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        
        gbc.gridx = 0; gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        contentPanel.add(buttonPanel, gbc);
        
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Cria um campo de texto estilizado com placeholder.
     */
    private JTextField createStyledTextField(String text, String placeholder) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        if (text.isEmpty()) {
            field.setForeground(Color.GRAY);
            field.setText(placeholder);
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(Color.BLACK);
                    }
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (field.getText().isEmpty()) {
                        field.setForeground(Color.GRAY);
                        field.setText(placeholder);
                    }
                }
            });
        }
        
        return field;
    }

    /**
     * Aplica estilo ao ComboBox.
     */
    private void stylizeComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
    }

    /**
     * Cria um JLabel estilizado para campos do formulário.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    /**
     * Inicializa listeners dos botões e campos.
     */
    @SuppressWarnings("unused")
    private void setupListeners() {
        btnAdicionarCategoria.addActionListener(e -> adicionarCategoria());
        btnAdicionarMarca.addActionListener(e -> adicionarMarca());
        btnSalvar.addActionListener(e -> salvarProduto());
        btnCancelar.addActionListener(e -> dispose());
        
        btnAdicionarCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdicionarCategoria.setBackground(PRIMARY_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdicionarCategoria.setBackground(PRIMARY_COLOR);
            }
        });
        
        btnAdicionarMarca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdicionarMarca.setBackground(PRIMARY_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdicionarMarca.setBackground(PRIMARY_COLOR);
            }
        });
        
        btnSalvar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(ACCENT_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalvar.setBackground(ACCENT_COLOR);
            }
        });
        
        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(DANGER_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCancelar.setBackground(DANGER_COLOR);
            }
        });
    }

    /**
     * Carrega categorias e marcas nos ComboBoxes.
     */
    private void carregarDados() {
        List<Categoria> categorias = categoriaRepository.listar();
        cbCategoria.removeAllItems();
        for (Categoria categoria : categorias) {
            cbCategoria.addItem(categoria);
        }
        
        List<Marca> marcas = marcaRepository.listar();
        cbMarca.removeAllItems();
        for (Marca marca : marcas) {
            cbMarca.addItem(marca);
        }
    }

    /**
     * Preenche os campos do formulário com os dados do produto original.
     */
    private void preencherDados() {
        if (produtoOriginal != null) {
            txtDescricao.setText(produtoOriginal.getDescricao());
            txtPreco.setText(String.format("%.2f", produtoOriginal.getPreco()).replace(",", "."));
            txtQuantidade.setText(String.valueOf(produtoOriginal.getQtd()));
            
            cbCategoria.setSelectedItem(produtoOriginal.getCategoria());
            cbMarca.setSelectedItem(produtoOriginal.getMarca());
        }
    }

    /**
     * Adiciona uma nova categoria ao repositório e ComboBox.
     */
    private void adicionarCategoria() {
        String nomeCategoria = JOptionPane.showInputDialog(this, 
            "Digite o nome da nova categoria:", 
            "Nova Categoria", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (nomeCategoria != null && !nomeCategoria.trim().isEmpty()) {
            Categoria novaCategoria = new Categoria(nomeCategoria.trim());
            categoriaRepository.adicionar(novaCategoria);
            cbCategoria.addItem(novaCategoria);
            cbCategoria.setSelectedItem(novaCategoria);
        }
    }

    /**
     * Adiciona uma nova marca ao repositório e ComboBox.
     */
    private void adicionarMarca() {
        String nomeMarca = JOptionPane.showInputDialog(this, 
            "Digite o nome da nova marca:", 
            "Nova Marca", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (nomeMarca != null && !nomeMarca.trim().isEmpty()) {
            Marca novaMarca = new Marca(nomeMarca.trim());
            marcaRepository.adicionar(novaMarca);
            cbMarca.addItem(novaMarca);
            cbMarca.setSelectedItem(novaMarca);
        }
    }

    /**
     * Salva as alterações do produto após validação dos campos.
     */
    private void salvarProduto() {
        try {
            String descricao = txtDescricao.getText().trim();
            if (descricao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrição é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Categoria categoria = (Categoria) cbCategoria.getSelectedItem();
            if (categoria == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma categoria!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Marca marca = (Marca) cbMarca.getSelectedItem();
            if (marca == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma marca!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double preco;
            try {
                preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                if (preco <= 0) {
                    JOptionPane.showMessageDialog(this, "Preço deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Preço inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int quantidade;
            try {
                quantidade = Integer.parseInt(txtQuantidade.getText());
                if (quantidade < 0) {
                    JOptionPane.showMessageDialog(this, "Quantidade deve ser maior ou igual a zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            produtoOriginal.setDescricao(descricao);
            produtoOriginal.setCategoria(categoria);
            produtoOriginal.setMarca(marca);
            produtoOriginal.setPreco(preco);
            produtoOriginal.setQtd(quantidade);
            
            produtoRepository.atualizar(produtoOriginal);
            
            produtoEditado = true;
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Indica se o produto foi editado com sucesso.
     * @return true se editado
     */
    public boolean isProdutoEditado() {
        return produtoEditado;
    }
} 