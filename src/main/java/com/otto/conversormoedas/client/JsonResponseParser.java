package com.otto.conversormoedas.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** Utilitário para demonstrar parsing JSON com Gson (JsonParser/JsonObject). */
public class JsonResponseParser {
    private final Gson gson = new Gson();

    /** Extrai conversion_rate manualmente (JsonParser). */
    public double parseConversionRate(String jsonResponse) {
        JsonElement elemento = JsonParser.parseString(jsonResponse);
        JsonObject objectRoot = elemento.getAsJsonObject();
        
        // Acessa o campo conversion_rate
        if (objectRoot.has("conversion_rate")) {
            return objectRoot.get("conversion_rate").getAsDouble();
        }
        throw new IllegalArgumentException("JSON sem o campo 'conversion_rate'");
    }

    /** Faz o parse completo para ExchangePairResponse (Gson). */
    public ExchangePairResponse parseFullResponse(String jsonResponse) {
        // Inspeciona a estrutura manualmente
        JsonElement elemento = JsonParser.parseString(jsonResponse);
        JsonObject objectRoot = elemento.getAsJsonObject();
        
        // Verifica campo result
        if (objectRoot.has("result")) {
            String result = objectRoot.get("result").getAsString();
            if (!"success".equalsIgnoreCase(result)) {
                String errorType = objectRoot.has("error-type") ? objectRoot.get("error-type").getAsString() : "unknown";
                throw new IllegalArgumentException("API retornou erro: " + errorType);
            }
        }
        
        // Depois desserializa automaticamente (Gson)
        return gson.fromJson(jsonResponse, ExchangePairResponse.class);
    }

    /** Extrai um campo específico do JSON. */
    public String extractField(String jsonResponse, String fieldName) {
        JsonElement elemento = JsonParser.parseString(jsonResponse);
        JsonObject objectRoot = elemento.getAsJsonObject();
        
        if (objectRoot.has(fieldName)) {
            return objectRoot.get(fieldName).getAsString();
        }
        return null;
    }
}
