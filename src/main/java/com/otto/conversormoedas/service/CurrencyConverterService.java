package com.otto.conversormoedas.service;

import com.otto.conversormoedas.client.ExchangePairResponse;
import com.otto.conversormoedas.client.ExchangeRateClient;
import com.otto.conversormoedas.client.ExchangeRateException;
import com.otto.conversormoedas.model.CurrencyCode;
import com.otto.conversormoedas.util.ConversionCalculator;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço de conversão que orquestra: chamadas à API, cache com TTL e cálculos.
 * Cache: chave FROM:TO (ex.: USD:BRL), TTL padrão 5 min, fallback para cache em falha.
 * Resiliência: usa cache válido se a API falhar; sem cache válido, lança exceção.
 */
public class CurrencyConverterService {
    private final ExchangeRateClient client;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;

    /**
     * Construtor principal permitindo definir um TTL customizado para o cache.
     * @param client cliente HTTP já configurado (não pode ser null)
     * @param ttlMillis tempo de vida (em milissegundos) de cada taxa armazenada
     */
    public CurrencyConverterService(ExchangeRateClient client, long ttlMillis) {
        this.client = Objects.requireNonNull(client);
        this.ttlMillis = ttlMillis;
    }

    /**
     * Construtor de conveniência usando TTL padrão de 5 minutos.
     * @param client cliente HTTP já configurado
     */
    public CurrencyConverterService(ExchangeRateClient client) {
        this(client, 5 * 60 * 1000); // default 5 minutes
    }

    private String key(String from, String to) {
        return from.toUpperCase() + ":" + to.toUpperCase();
    }

    /**
     * Obtém a taxa (from→to) usando cache com TTL; em falha usa cache válido.
     */
    public double getRate(String from, String to) {
        String k = key(from, to);
        CacheEntry entry = cache.get(k);
        long now = Instant.now().toEpochMilli();
        if (entry != null && (now - entry.fetchedAtMillis) < ttlMillis) {
            return entry.rate;
        }

        try {
            ExchangePairResponse resp = client.fetchPair(from.toUpperCase(), to.toUpperCase());
            double rate = resp.conversion_rate;
            cache.put(k, new CacheEntry(rate, now));
            return rate;
        } catch (IOException | InterruptedException | ExchangeRateException e) {
            // If we have a cached value, return it as a best-effort fallback
            if (entry != null) {
                return entry.rate;
            }
            throw new RuntimeException("Failed to fetch exchange rate for " + k, e);
        }
    }

    /**
     * Converte usando taxa atual (getRate) e cálculo (ConversionCalculator).
     */
    public double convert(double amount, String from, String to) {
        double rate = getRate(from, to);
        return ConversionCalculator.convert(amount, rate);
    }

    /**
     * Converte e retorna dados detalhados (valor, moedas, taxa e inversa).
     */
    public ConversionCalculator.ConversionResult convertDetailed(double amount, String from, String to) {
        double rate = getRate(from, to);
        double converted = ConversionCalculator.convert(amount, rate);
        return new ConversionCalculator.ConversionResult(amount, from, converted, to, rate);
    }

    /**
     * Sobrecarga que aceita {@link CurrencyCode} em vez de {@link String} para segurança de tipos.
     * @see #getRate(String, String)
     */
    public double getRate(CurrencyCode from, CurrencyCode to) {
        return getRate(from.name(), to.name());
    }

    /**
     * Sobrecarga typed-safe de {@link #convert(double, String, String)}.
     */
    public double convert(CurrencyCode from, CurrencyCode to, double amount) {
        return convert(amount, from.name(), to.name());
    }

    /**
     * Sobrecarga typed-safe de {@link #convertDetailed(double, String, String)}.
     */
    public ConversionCalculator.ConversionResult convertDetailed(CurrencyCode from, CurrencyCode to, double amount) {
        return convertDetailed(amount, from.name(), to.name());
    }

    /**
     * Estrutura interna do cache contendo a taxa e o timestamp da captura.
     */
    private static class CacheEntry {
        final double rate;
        final long fetchedAtMillis;

        CacheEntry(double rate, long fetchedAtMillis) {
            this.rate = rate;
            this.fetchedAtMillis = fetchedAtMillis;
        }
    }
}
