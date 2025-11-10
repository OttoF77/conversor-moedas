package com.otto.conversormoedas.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.otto.conversormoedas.config.Config;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cliente HTTP para a ExchangeRate-API (v6).
 * Responsável por: montar requisições, ler API Key (Config), tratar erros HTTP/API
 * e desserializar JSON (Gson) para {@link ExchangePairResponse}.
 * Observação: uso apenas no servidor (API Key nunca vai ao frontend).
 */
public class ExchangeRateClient {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6";
    private static final Logger LOGGER = Logger.getLogger(ExchangeRateClient.class.getName());

    private final String apiKey;
    private final HttpClient httpClient;
    private final Gson gson = new Gson();

    /**
     * Constrói o cliente com API Key (Config) e HttpClient com timeout de 10s.
     * Lança IllegalStateException se a chave não estiver configurada.
     */
    public ExchangeRateClient() {
        this.apiKey = Config.getExchangeRateApiKey();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Busca dados para um par (ex.: "USD" → "BRL").
     * Síncrono. Lança IOException/InterruptedException em rede.
     * Lança ExchangeRateException para status != 200 ou erro da API.
     */
    public ExchangePairResponse fetchPair(String from, String to) throws IOException, InterruptedException, ExchangeRateException {
        String endpoint = String.format("%s/%s/pair/%s/%s", BASE_URL, apiKey, from, to);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .header("User-Agent", "conversor-moedas-java-client/0.1")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();
        // Log some useful headers for diagnostics (if present)
        String requestId = response.headers().firstValue("x-request-id").orElse(null);
        response.headers().firstValue("rate-limit").ifPresent(h -> LOGGER.fine("rate-limit: " + h));
        if (requestId != null) {
            LOGGER.fine("x-request-id: " + requestId);
        }

        String body = response.body();

        if (status != 200) {
            // Try to extract API error message from body
            String apiMessage = extractApiErrorMessage(body);
            String msg = String.format("ExchangeRate API returned HTTP %d. %s", status, apiMessage == null ? "" : apiMessage);
            throw new ExchangeRateException(msg);
        }

        // Faz o parse do JSON e valida o campo de resultado da API, quando presente
        try {
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            if (json.has("result")) {
                String result = json.get("result").getAsString();
                if (!"success".equalsIgnoreCase(result) && !"ok".equalsIgnoreCase(result)) {
                    String apiMessage = extractApiErrorMessage(body);
                    throw new ExchangeRateException("ExchangeRate API error: " + (apiMessage != null ? apiMessage : result));
                }
            }
            ExchangePairResponse pair = gson.fromJson(body, ExchangePairResponse.class);
            return pair;
        } catch (com.google.gson.JsonSyntaxException ex) {
            LOGGER.log(Level.WARNING, "Failed to parse JSON response", ex);
            throw new ExchangeRateException("Invalid JSON response from ExchangeRate API");
        }
    }

    /**
     * Extrai mensagem de erro do JSON (ordem: error-type, error, message).
     */
    private String extractApiErrorMessage(String body) {
        try {
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            if (json.has("error-type")) return json.get("error-type").getAsString();
            if (json.has("error")) return json.get("error").getAsString();
            if (json.has("message")) return json.get("message").getAsString();
        } catch (Exception e) {
            // ignore parsing errors here
        }
        return null;
    }
}

