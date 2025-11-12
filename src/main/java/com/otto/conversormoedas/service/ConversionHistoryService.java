package com.otto.conversormoedas.service;

import com.otto.conversormoedas.model.ConversionHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Gerencia o histórico de conversões realizadas.
 * Armazena em memória as últimas conversões (limitado a MAX_HISTORY_SIZE).
 */
public class ConversionHistoryService {
    private static final Logger LOGGER = Logger.getLogger(ConversionHistoryService.class.getName());
    private static final int MAX_HISTORY_SIZE = 50; // Limita a 50 últimas conversões
    
    private final List<ConversionHistory> history = Collections.synchronizedList(new ArrayList<>());

    /**
     * Adiciona uma conversão ao histórico.
     * Remove a mais antiga se atingir o limite.
     */
    public void addConversion(String fromCurrency, String toCurrency, 
                             double amount, double result, double exchangeRate) {
        ConversionHistory record = new ConversionHistory(
            fromCurrency, toCurrency, amount, result, exchangeRate
        );
        
        synchronized (history) {
            // Remove a mais antiga se atingir o limite
            if (history.size() >= MAX_HISTORY_SIZE) {
                history.remove(0);
            }
            history.add(record);
        }
        
        // Log da conversão
        LOGGER.info(String.format("💱 Conversão registrada: %s", record));
    }

    /**
     * Retorna o histórico completo (cópia imutável).
     */
    public List<ConversionHistory> getHistory() {
        synchronized (history) {
            return new ArrayList<>(history);
        }
    }

    /**
     * Retorna as últimas N conversões.
     */
    public List<ConversionHistory> getLastConversions(int limit) {
        synchronized (history) {
            int size = history.size();
            int fromIndex = Math.max(0, size - limit);
            return new ArrayList<>(history.subList(fromIndex, size));
        }
    }

    /**
     * Limpa o histórico.
     */
    public void clearHistory() {
        synchronized (history) {
            history.clear();
        }
        LOGGER.info("🗑️ Histórico de conversões limpo");
    }

    /**
     * Retorna quantidade de conversões no histórico.
     */
    public int getHistorySize() {
        return history.size();
    }
}
