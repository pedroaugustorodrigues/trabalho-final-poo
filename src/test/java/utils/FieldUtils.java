package test.java.utils;

import main.java.model.Produto;
import main.java.model.Usuario;

import java.lang.reflect.Field;

public class FieldUtils {
    public static void setIdManualmente(Produto produto, int idDesejado) {
        try {
            Field idField = Produto.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.setInt(produto, idDesejado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir ID manualmente", e);
        }
    }

    public static void setIdManualmente(Usuario usuario, int idDesejado) {
        try {
            Field idField = Usuario.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.setInt(usuario, idDesejado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir ID do usuário manualmente", e);
        }
    }
}
