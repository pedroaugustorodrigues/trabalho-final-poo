package main.java.app;

import main.java.repository.UsuarioRepository;
import main.java.service.AutenticacaoService;
import main.java.view.LoginGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        
        UsuarioRepository usuarioRepository = new UsuarioRepository();

        
        AutenticacaoService autenticacaoService = new AutenticacaoService(usuarioRepository);

        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginGUI loginGUI = new LoginGUI(autenticacaoService);
                loginGUI.setVisible(true);
            }
        });
    }
}
