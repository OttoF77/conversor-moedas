package com.otto.conversormoedas.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para a classe ConversionCalculator.
 * Demonstra a lógica de conversão da etapa_9.
 */
public class ConversionCalculatorTest {

    private static final double DELTA = 0.0001; // margem de erro para comparação de doubles

    @Test
    public void convert_validInput_returnsCorrectConversion() {
        // Cenário: 100 BRL com taxa de 0.20 USD/BRL
        double result = ConversionCalculator.convert(100.0, 0.20);
        assertEquals(20.0, result, DELTA);
    }

    @Test
    public void convert_zeroAmount_returnsZero() {
        double result = ConversionCalculator.convert(0.0, 5.5);
        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void convert_negativeAmount_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            ConversionCalculator.convert(-100.0, 0.20)
        );
    }

    @Test
    public void convert_negativeRate_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            ConversionCalculator.convert(100.0, -0.20)
        );
    }

    @Test
    public void reverseConversion_validInput_returnsOriginalAmount() {
        // Cenário: 20 USD convertido de volta para BRL (taxa original: 0.20)
        double result = ConversionCalculator.reverseConversion(20.0, 0.20);
        assertEquals(100.0, result, DELTA);
    }

    @Test
    public void reverseConversion_zeroRate_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            ConversionCalculator.reverseConversion(20.0, 0.0)
        );
    }

    @Test
    public void getInverseRate_validRate_returnsInverse() {
        // Se 1 BRL = 0.20 USD, então 1 USD = 5.0 BRL
        double inverseRate = ConversionCalculator.getInverseRate(0.20);
        assertEquals(5.0, inverseRate, DELTA);
    }

    @Test
    public void getInverseRate_zeroRate_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            ConversionCalculator.getInverseRate(0.0)
        );
    }

    @Test
    public void calculatePercentageDifference_higherConverted_returnsPositive() {
        // 100 -> 150 = +50%
        double diff = ConversionCalculator.calculatePercentageDifference(100.0, 150.0);
        assertEquals(50.0, diff, DELTA);
    }

    @Test
    public void calculatePercentageDifference_lowerConverted_returnsNegative() {
        // 100 -> 80 = -20%
        double diff = ConversionCalculator.calculatePercentageDifference(100.0, 80.0);
        assertEquals(-20.0, diff, DELTA);
    }

    @Test
    public void calculatePercentageDifference_sameValue_returnsZero() {
        double diff = ConversionCalculator.calculatePercentageDifference(100.0, 100.0);
        assertEquals(0.0, diff, DELTA);
    }

    @Test
    public void round_twoDecimalPlaces_roundsCorrectly() {
        double result = ConversionCalculator.round(123.456789, 2);
        assertEquals(123.46, result, DELTA);
    }

    @Test
    public void round_zeroDecimalPlaces_roundsToInteger() {
        double result = ConversionCalculator.round(123.456, 0);
        assertEquals(123.0, result, DELTA);
    }

    @Test
    public void round_negativeDecimalPlaces_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            ConversionCalculator.round(123.45, -1)
        );
    }

    @Test
    public void conversionResult_creation_storesAllData() {
        ConversionCalculator.ConversionResult result = 
            new ConversionCalculator.ConversionResult(100.0, "BRL", 20.0, "USD", 0.20);
        
        assertEquals(100.0, result.getOriginalAmount(), DELTA);
        assertEquals("BRL", result.getFromCurrency());
        assertEquals(20.0, result.getConvertedAmount(), DELTA);
        assertEquals("USD", result.getToCurrency());
        assertEquals(0.20, result.getExchangeRate(), DELTA);
        assertEquals(5.0, result.getInverseRate(), DELTA); // 1/0.20 = 5.0
    }

    @Test
    public void conversionResult_toString_formatsCorrectly() {
        ConversionCalculator.ConversionResult result = 
            new ConversionCalculator.ConversionResult(100.0, "BRL", 20.0, "USD", 0.20);
        
        String output = result.toString();
        // Verifica se contém os elementos principais (independente de localização)
        assertTrue(output.contains("BRL"));
        assertTrue(output.contains("USD"));
        assertTrue(output.contains("taxa"));
        // Aceita tanto ponto quanto vírgula como separador decimal
        assertTrue(output.contains("100") && (output.contains("100.00") || output.contains("100,00")));
    }

    /**
     * Teste de demonstração da etapa_9: conversão completa BRL → USD
     */
    @Test
    public void etapa9_demonstracao_conversaoBrlParaUsd() {
        // Dados de entrada (valores inseridos pelo usuário)
        double valorOriginal = 250.50;
        String moedaOrigem = "BRL";
        String moedaDestino = "USD";
        double taxaDeCambio = 0.201234; // taxa obtida da API
        
        // Aplicar a taxa de conversão
        double valorConvertido = ConversionCalculator.convert(valorOriginal, taxaDeCambio);
        
        // Arredondar para exibição
        double valorFinal = ConversionCalculator.round(valorConvertido, 2);
        
        // Verificações
        assertEquals(50.41, valorFinal, DELTA);
        
        // Criar resultado detalhado
        ConversionCalculator.ConversionResult resultado = 
            new ConversionCalculator.ConversionResult(
                valorOriginal, moedaOrigem, valorConvertido, moedaDestino, taxaDeCambio
            );
        
        // Verificar que todas as peças se encaixam
        assertNotNull(resultado);
        assertEquals(valorOriginal, resultado.getOriginalAmount(), DELTA);
        assertEquals(moedaOrigem, resultado.getFromCurrency());
        assertEquals(valorConvertido, resultado.getConvertedAmount(), DELTA);
        assertEquals(moedaDestino, resultado.getToCurrency());
        assertEquals(taxaDeCambio, resultado.getExchangeRate(), DELTA);
        
        System.out.println("✅ Etapa 9 - Demonstração de conversão:");
        System.out.println(resultado.toString());
    }
}
