package com.otto.conversormoedas;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testes básicos para a aplicação.
 */
public class AppTest {

    @Test
    void mainMethodExists() {
        // Verifica que a classe App tem o método main
        try {
            App.class.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            Assertions.fail("Método main não encontrado na classe App");
        }
    }
}
