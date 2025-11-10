package com.otto.conversormoedas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o enum CurrencyCode.
 */
public class CurrencyCodeTest {

    @Test
    public void isValid_validCode_returnsTrue() {
        assertTrue(CurrencyCode.isValid("BRL"));
        assertTrue(CurrencyCode.isValid("USD"));
        assertTrue(CurrencyCode.isValid("ars")); // case-insensitive
    }

    @Test
    public void isValid_invalidCode_returnsFalse() {
        assertFalse(CurrencyCode.isValid("XYZ"));
        assertFalse(CurrencyCode.isValid(""));
        assertFalse(CurrencyCode.isValid(null));
    }

    @Test
    public void fromString_validCode_returnsEnum() {
        assertEquals(CurrencyCode.BRL, CurrencyCode.fromString("BRL"));
        assertEquals(CurrencyCode.USD, CurrencyCode.fromString("usd")); // case-insensitive
        assertEquals(CurrencyCode.ARS, CurrencyCode.fromString("ARS"));
    }

    @Test
    public void fromString_invalidCode_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> CurrencyCode.fromString("XYZ"));
        assertThrows(IllegalArgumentException.class, () -> CurrencyCode.fromString(""));
        assertThrows(IllegalArgumentException.class, () -> CurrencyCode.fromString(null));
    }

    @Test
    public void toString_returnsCodeAndDescription() {
        String result = CurrencyCode.BRL.toString();
        assertTrue(result.contains("BRL"));
        assertTrue(result.contains("Real brasileiro"));
    }

    @Test
    public void getDescription_returnsCorrectDescription() {
        assertEquals("Real brasileiro", CurrencyCode.BRL.getDescription());
        assertEquals("Dólar americano", CurrencyCode.USD.getDescription());
        assertEquals("Peso argentino", CurrencyCode.ARS.getDescription());
    }

    @Test
    public void allRequiredCurrencies_areDefined() {
        // Verifica que as 6 moedas obrigatórias da etapa_8 estão presentes
        assertNotNull(CurrencyCode.valueOf("ARS"));
        assertNotNull(CurrencyCode.valueOf("BOB"));
        assertNotNull(CurrencyCode.valueOf("BRL"));
        assertNotNull(CurrencyCode.valueOf("CLP"));
        assertNotNull(CurrencyCode.valueOf("COP"));
        assertNotNull(CurrencyCode.valueOf("USD"));
        
        // Verifica que temos pelo menos 6 moedas
        assertTrue(CurrencyCode.values().length >= 6);
    }
}
