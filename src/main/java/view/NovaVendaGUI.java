package main.java.view;

import main.java.model.Cliente;
import main.java.model.ItemVenda;
import main.java.model.Produto;
import main.java.model.Venda;
import main.java.repository.ClienteRepository;
import main.java.repository.ProdutoRepository;
import main.java.repository.VendaRepository;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.SpinnerNumberModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Modal para registrar uma nova venda.
 * Permite buscar cliente, adicionar produtos ao carrinho, calcular total e finalizar a venda.
 *
 * @author Pedro
 */
public class NovaVendaGUI extends JDialog {
    private final ClienteRepository clienteRepository = new ClienteRepository();
    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final VendaRepository vendaRepository = new VendaRepository();

    private Cliente clienteSelecionado;
    private Produto produtoSelecionado;
    private final List<ItemVenda> carrinho = new ArrayList<>();

    private JTextField cpfField, nomeClienteField;
    private JTextField codProdutoField, nomeProdutoField, marcaField, precoField;
    private JSpinner qtdSpinner;
    private JLabel estoqueLabel;
    private JLabel totalVendaLabel;
    private DefaultTableModel carrinhoTableModel;
    private JTable carrinhoTable;

    /**
     * Cria o modal de nova venda.
     * @param owner janela pai (Frame)
     */
    public NovaVendaGUI(Frame owner) {
        super(owner, "Nova Venda", true);
        initComponents();
    }

    /**
     * Cria o modal de nova venda.
     * @param owner janela pai (Dialog)
     */
    public NovaVendaGUI(Dialog owner) {
        super(owner, "Nova Venda", true);
        initComponents();
    }

    /**
     * Inicializa os componentes e layout da tela.
     */
    private void initComponents() {
        setSize(900, 650);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        topPanel.add(createClientePanel());
        topPanel.add(createProdutoPanel());

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(createCarrinhoPanel(), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(createTotalPanel(), BorderLayout.WEST);
        bottomPanel.add(createActionsPanel(), BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Cria o painel de busca e exibição de dados do cliente.
     */
    @SuppressWarnings("unused")
    private JPanel createClientePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Dados do Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        cpfField = new JTextField(14);
        panel.add(cpfField, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        JButton buscarClienteBtn = new JButton("Pesquisar");
        buscarClienteBtn.addActionListener(e -> buscarCliente());
        panel.add(buscarClienteBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        nomeClienteField = new JTextField();
        nomeClienteField.setEditable(false);
        panel.add(nomeClienteField, gbc);
        
        return panel;
    }

    /**
     * Cria o painel de busca e exibição de dados do produto.
     */
    @SuppressWarnings("unused")
    private JPanel createProdutoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Dados do Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        codProdutoField = new JTextField(10);
        panel.add(codProdutoField, gbc);

        gbc.gridx = 3; gbc.gridy = 0;
        JButton buscarProdutoBtn = new JButton("Pesquisar");
        buscarProdutoBtn.addActionListener(e -> buscarProduto());
        panel.add(buscarProdutoBtn, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Produto:"), gbc);
        nomeProdutoField = new JTextField(); nomeProdutoField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; panel.add(nomeProdutoField, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Marca:"), gbc);
        marcaField = new JTextField(); marcaField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; panel.add(marcaField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Preço:"), gbc);
        precoField = new JTextField(); precoField.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(precoField, gbc);

        gbc.gridx = 2; gbc.gridy = 3; panel.add(new JLabel("Qtd:"), gbc);
        qtdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        gbc.gridx = 3; gbc.gridy = 3; panel.add(qtdSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Estoque:"), gbc);
        estoqueLabel = new JLabel("0 unidades");
        estoqueLabel.setForeground(new Color(100, 100, 100));
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 3; panel.add(estoqueLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        JButton addCarrinhoBtn = new JButton("Adicionar ao Carrinho");
        addCarrinhoBtn.addActionListener(e -> adicionarAoCarrinho());
        panel.add(addCarrinhoBtn, gbc);

        return panel;
    }

    /**
     * Cria o painel do carrinho de compras.
     */
    private JPanel createCarrinhoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Carrinho de Compras"));
        String[] columns = {"Código", "Produto", "Marca", "Preço Unit.", "Qtd", "Subtotal"};
        carrinhoTableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        carrinhoTable = new JTable(carrinhoTableModel);
        panel.add(new JScrollPane(carrinhoTable), BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Cria o painel que exibe o total da venda.
     */
    private JPanel createTotalPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new TitledBorder("Total da Venda"));
        totalVendaLabel = new JLabel("R$ 0,00");
        totalVendaLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        panel.add(totalVendaLabel);
        return panel;
    }

    /**
     * Cria o painel de ações (pagamento/cancelar).
     */
    @SuppressWarnings("unused")
    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton limparCarrinhoBtn = new JButton("Limpar Carrinho");
        limparCarrinhoBtn.setBackground(new Color(255, 193, 7));
        limparCarrinhoBtn.setForeground(Color.WHITE);
        limparCarrinhoBtn.addActionListener(e -> limparCarrinho());
        
        JButton pagamentoBtn = new JButton("Pagamento");
        pagamentoBtn.setBackground(new Color(46, 204, 113));
        pagamentoBtn.setForeground(Color.WHITE);
        pagamentoBtn.addActionListener(e -> finalizarVenda());

        JButton cancelarBtn = new JButton("Cancelar Venda");
        cancelarBtn.setBackground(new Color(231, 76, 60));
        cancelarBtn.setForeground(Color.WHITE);
        cancelarBtn.addActionListener(e -> dispose());
        
        panel.add(limparCarrinhoBtn);
        panel.add(pagamentoBtn);
        panel.add(cancelarBtn);
        return panel;
    }

    /**
     * Busca o cliente pelo CPF informado.
     */
    private void buscarCliente() {
        String cpf = cpfField.getText();
        Optional<Cliente> clienteOpt = clienteRepository.listar().stream()
                .filter(c -> c.getCpf().equals(cpf)).findFirst();
        if (clienteOpt.isPresent()) {
            clienteSelecionado = clienteOpt.get();
            nomeClienteField.setText(clienteSelecionado.getNome());
        } else {
            JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
            clienteSelecionado = null;
            nomeClienteField.setText("");
        }
    }

    /**
     * Busca o produto pelo código informado.
     */
    private void buscarProduto() {
        try {
            int cod = Integer.parseInt(codProdutoField.getText());
            Optional<Produto> produtoOpt = produtoRepository.buscarPorId(cod);
            if (produtoOpt.isPresent()) {
                produtoSelecionado = produtoOpt.get();
                nomeProdutoField.setText(produtoSelecionado.getDescricao());
                marcaField.setText(produtoSelecionado.getMarca().getNome());
                precoField.setText(String.format("%.2f", produtoSelecionado.getPreco()));
                
                // Atualizar o spinner de quantidade baseado no estoque disponível
                int estoqueDisponivel = produtoSelecionado.getQtd();
                qtdSpinner.setModel(new SpinnerNumberModel(1, 1, estoqueDisponivel, 1));
                estoqueLabel.setText(estoqueDisponivel + " unidades disponíveis");
                estoqueLabel.setForeground(estoqueDisponivel > 0 ? new Color(32, 201, 151) : new Color(230, 86, 86));
                
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado.");
                produtoSelecionado = null;
                qtdSpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
                estoqueLabel.setText("0 unidades");
                estoqueLabel.setForeground(new Color(100, 100, 100));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código do produto inválido.");
        }
    }

    /**
     * Adiciona o produto selecionado ao carrinho.
     */
    private void adicionarAoCarrinho() {
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.");
            return;
        }
        try {
            int qtd = (int) qtdSpinner.getValue();
            if(qtd <= 0) {
                 JOptionPane.showMessageDialog(this, "Quantidade deve ser positiva.");
                 return;
            }
            if(qtd > produtoSelecionado.getQtd()) {
                JOptionPane.showMessageDialog(this, "Quantidade em estoque insuficiente.");
                return;
            }

            ItemVenda item = new ItemVenda(produtoSelecionado, qtd);
            carrinho.add(item);
            
            carrinhoTableModel.addRow(new Object[]{
                item.getProduto().getId(),
                item.getProduto().getDescricao(),
                item.getProduto().getMarca().getNome(),
                String.format("R$ %.2f", item.getProduto().getPreco()),
                item.getQuantidade(),
                String.format("R$ %.2f", item.getSubtotal())
            });

            // Atualizar estoque disponível
            int novoEstoque = produtoSelecionado.getQtd() - qtd;
            estoqueLabel.setText(novoEstoque + " unidades disponíveis");
            estoqueLabel.setForeground(novoEstoque > 0 ? new Color(32, 201, 151) : new Color(230, 86, 86));
            
            // Atualizar spinner se necessário
            if (novoEstoque > 0) {
                qtdSpinner.setModel(new SpinnerNumberModel(1, 1, novoEstoque, 1));
            } else {
                qtdSpinner.setModel(new SpinnerNumberModel(0, 0, 0, 1));
            }

            atualizarTotal();
            limparCamposProduto();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
        }
    }

    /**
     * Atualiza o valor total da venda.
     */
    private void atualizarTotal() {
        double total = carrinho.stream().mapToDouble(ItemVenda::getSubtotal).sum();
        totalVendaLabel.setText(String.format("R$ %.2f", total));
    }
    
    /**
     * Limpa os campos do produto após adicionar ao carrinho.
     */
    private void limparCamposProduto() {
        produtoSelecionado = null;
        codProdutoField.setText("");
        nomeProdutoField.setText("");
        marcaField.setText("");
        precoField.setText("");
        qtdSpinner.setValue(1);
        estoqueLabel.setText("0 unidades");
        estoqueLabel.setForeground(new Color(100, 100, 100));
    }

    /**
     * Limpa todos os itens do carrinho.
     */
    private void limparCarrinho() {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O carrinho já está vazio.");
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja limpar o carrinho?",
            "Confirmar Limpeza",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirmacao == JOptionPane.YES_OPTION) {
            carrinho.clear();
            carrinhoTableModel.setRowCount(0);
            atualizarTotal();
            JOptionPane.showMessageDialog(this, "Carrinho limpo com sucesso!");
        }
    }

    /**
     * Finaliza a venda, atualiza estoque e salva no repositório.
     */
    private void finalizarVenda() {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O carrinho está vazio.");
            return;
        }

        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para a venda.");
            return;
        }

        // Criar resumo da venda
        StringBuilder resumo = new StringBuilder();
        resumo.append("RESUMO DA VENDA\n\n");
        resumo.append("Cliente: ").append(clienteSelecionado.getNome()).append("\n");
        resumo.append("CPF: ").append(clienteSelecionado.getCpf()).append("\n\n");
        resumo.append("ITENS:\n");
        
        double total = 0;
        for (ItemVenda item : carrinho) {
            resumo.append("• ").append(item.getProduto().getDescricao())
                  .append(" - Qtd: ").append(item.getQuantidade())
                  .append(" - R$ ").append(String.format("%.2f", item.getSubtotal())).append("\n");
            total += item.getSubtotal();
        }
        
        resumo.append("\nTOTAL: R$ ").append(String.format("%.2f", total));
        
        int confirmacao = JOptionPane.showConfirmDialog(this,
            resumo.toString(),
            "Confirmar Venda",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
            
        if (confirmacao == JOptionPane.YES_OPTION) {
            Venda novaVenda = new Venda(clienteSelecionado, new ArrayList<>(carrinho));
            
            for(ItemVenda item : carrinho) {
                Produto p = item.getProduto();
                p.setQtd(p.getQtd() - item.getQuantidade());
                produtoRepository.atualizar(p);
            }

            vendaRepository.adicionar(novaVenda);
            
            // Atualizar dashboard do gestor em tempo real
            main.java.view.GestorDashboardGUI.atualizarDashboardSeAtivo();
            
            JOptionPane.showMessageDialog(this, "Venda realizada com sucesso!");
            dispose();
        }
    }
}
