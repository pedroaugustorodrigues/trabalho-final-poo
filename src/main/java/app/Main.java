package main.java.app;

import main.java.repository.UsuarioRepository;
import main.java.service.AutenticacaoService;
import main.java.view.LoginGUI;
import main.java.model.Gestor;
import main.java.model.Cliente;

import javax.swing.*;

/**
 * Classe principal da aplicação.
 * Inicializa repositórios, cria usuários padrão e exibe a tela de login.
 *
 * @author Pedro
 */
public class Main {
    /**
     * Método principal. Inicializa o sistema e exibe a tela de login.
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        AutenticacaoService autenticacaoService = new AutenticacaoService(usuarioRepository);

        try {
            // Usuário Gestor padrão
            Gestor gestorPadrao = new Gestor("Administrador", "000.000.000-00", "admin", "admin");
            usuarioRepository.adicionarUsuario(gestorPadrao);
            
            // Usuário Cliente padrão
            Cliente clientePadrao = new Cliente("João Silva", "111.111.111-11", "cliente", "(11) 99999-9999", "cliente");
            usuarioRepository.adicionarUsuario(clientePadrao);
            
        } catch (Exception e) {
            System.out.println("Usuários padrão já existem ou erro ao criar: " + e.getMessage());
        }

        // Inicia a interface gráfica
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginGUI loginGUI = new LoginGUI(autenticacaoService);
                loginGUI.setVisible(true);
            }
        });
    }
}
