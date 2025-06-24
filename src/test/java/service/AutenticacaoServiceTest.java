package test.java.service;

import main.java.exceptions.AutenticacaoException;
import main.java.exceptions.RegistroDuplicadoException;
import main.java.model.Cliente;
import main.java.model.Gestor;
import main.java.model.Usuario;
import main.java.repository.UsuarioRepository;
import main.java.service.AutenticacaoService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoServiceTest {

    private UsuarioRepository usuarioRepository;
    private AutenticacaoService autenticacaoService;

    @BeforeEach
    void setup() {
        usuarioRepository = new UsuarioRepository();
        // Limpar o arquivo para evitar interferência
        java.io.File arquivo = new java.io.File("usuarios.dat");
        if (arquivo.exists()) {
            arquivo.delete();
        }
        // Resetar id estático da classe Usuario
        // (supondo que exista método estático para isso)
        main.java.model.Usuario.resetProximoId();

        autenticacaoService = new AutenticacaoService(usuarioRepository);
    }

    @Test
    void testCadastrarUsuarioClienteComSucesso() throws RegistroDuplicadoException {
        autenticacaoService.cadastrarUsuario("João", "123.456.789-00", "joao@email.com", "senha123", "cliente");

        Usuario usuario = usuarioRepository.buscarPorEmail("joao@email.com").orElse(null);
        assertNotNull(usuario);
        assertTrue(usuario instanceof Cliente);
        assertEquals("João", usuario.getNome());
    }

    @Test
    void testCadastrarUsuarioGestorComSucesso() throws RegistroDuplicadoException {
        autenticacaoService.cadastrarUsuario("Maria", "123.456.789-00", "maria@email.com", "senha123", "gestor");

        Usuario usuario = usuarioRepository.buscarPorEmail("maria@email.com").orElse(null);
        assertNotNull(usuario);
        assertTrue(usuario instanceof Gestor);
        assertEquals("Maria", usuario.getNome());
    }

    @Test
    void testCadastrarUsuarioComEmailDuplicadoDeveLancarExcecao() throws RegistroDuplicadoException {
        autenticacaoService.cadastrarUsuario("Ana", "123.456.789-00", "ana@email.com", "senha123", "cliente");

        RegistroDuplicadoException ex = assertThrows(RegistroDuplicadoException.class, () -> {
            autenticacaoService.cadastrarUsuario("Ana2", "123.456.789-00", "ana@email.com", "senha456", "cliente");
        });

        assertTrue(ex.getMessage().contains("Email já cadastrado"));
    }

    @Test
    void testLoginComSucesso() throws Exception {
        autenticacaoService.cadastrarUsuario("Pedro", "123.456.789-00", "pedro@email.com", "senhaSecreta", "cliente");

        Usuario usuario = autenticacaoService.login("pedro@email.com", "senhaSecreta");

        assertNotNull(usuario);
        assertEquals("Pedro", usuario.getNome());
    }

    @Test
    void testLoginComEmailIncorretoDeveLancarExcecao() {
        AutenticacaoException ex = assertThrows(AutenticacaoException.class, () -> {
            autenticacaoService.login("inexistente@email.com", "qualquer");
        });
        assertTrue(ex.getMessage().contains("Email ou senha inválidos"));
    }

    @Test
    void testLoginComSenhaIncorretaDeveLancarExcecao() throws RegistroDuplicadoException {
        autenticacaoService.cadastrarUsuario("Lucas", "123.456.789-00", "lucas@email.com", "senhaCerta", "gestor");

        AutenticacaoException ex = assertThrows(AutenticacaoException.class, () -> {
            autenticacaoService.login("lucas@email.com", "senhaErrada");
        });

        assertTrue(ex.getMessage().contains("Email ou senha inválidos"));
    }

    @Test
    void testAutenticarRetornaTrueParaCredenciaisValidas() throws RegistroDuplicadoException {
        autenticacaoService.cadastrarUsuario("Carla", "123.456.789-00", "carla@email.com", "123456", "cliente");

        assertTrue(autenticacaoService.autenticar("carla@email.com", "123456"));
    }

    @Test
    void testAutenticarRetornaFalseParaEmailInvalido() {
        assertFalse(autenticacaoService.autenticar("naoexiste@email.com", "senha"));
    }

    @Test
    void testAutenticarRetornaFalseParaSenhaInvalida() throws RegistroDuplicadoException {
        autenticacaoService.cadastrarUsuario("Pedro", "123.456.789-00", "pedro2@email.com", "senha123", "gestor");

        assertFalse(autenticacaoService.autenticar("pedro2@email.com", "senhaErrada"));
    }
}