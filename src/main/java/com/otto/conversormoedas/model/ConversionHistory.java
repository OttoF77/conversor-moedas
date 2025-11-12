package com.otto.conversormoedas.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um registro de conversão no histórico.
 * Armazena informações sobre conversões realizadas.
 */
public class ConversionHistory {
    private final String fromCurrency;
    private final String toCurrency;
    private final double amount;
    private final double result;
    private final double exchangeRate;
    private final LocalDateTime timestamp;

    public ConversionHistory(String fromCurrency, String toCurrency, double amount, 
                           double result, double exchangeRate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.result = result;
        this.exchangeRate = exchangeRate;
        this.timestamp = LocalDateTime.now();
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public double getResult() {
        return result;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Formata timestamp para exibição.
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return timestamp.format(formatter);
    }

    /**
     * Retorna descrição legível da conversão.
     */
    @Override
    public String toString() {
        return String.format("[%s] %.2f %s → %.2f %s (taxa: %.4f)",
            getFormattedTimestamp(), amount, fromCurrency, result, toCurrency, exchangeRate);
    }
}
