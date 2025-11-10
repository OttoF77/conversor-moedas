package com.otto.conversormoedas.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testes de parsing JSON com Gson (JsonParser/JsonObject).
 * Demonstra parsing manual conforme etapa_7.
 */
public class JsonResponseParserTest {

    @Test
    void parseConversionRate_validJson_returnsRate() {
        String jsonResponse = """
            {
                "result": "success",
                "base_code": "USD",
                "target_code": "BRL",
                "conversion_rate": 5.25
            }
            """;

        JsonResponseParser parser = new JsonResponseParser();
        double rate = parser.parseConversionRate(jsonResponse);
        
        Assertions.assertEquals(5.25, rate, 0.001);
    }

    @Test
    void parseFullResponse_validJson_returnsObject() {
        String jsonResponse = """
            {
                "result": "success",
                "documentation": "https://www.exchangerate-api.com/docs",
                "terms_of_use": "https://www.exchangerate-api.com/terms",
                "time_last_update_unix": 1699401600,
                "time_last_update_utc": "Thu, 08 Nov 2025 00:00:00 +0000",
                "time_next_update_unix": 1699488000,
                "time_next_update_utc": "Fri, 09 Nov 2025 00:00:00 +0000",
                "base_code": "USD",
                "target_code": "BRL",
                "conversion_rate": 5.25
            }
            """;

        JsonResponseParser parser = new JsonResponseParser();
        ExchangePairResponse response = parser.parseFullResponse(jsonResponse);
        
        Assertions.assertEquals("success", response.result);
        Assertions.assertEquals("USD", response.base_code);
        Assertions.assertEquals("BRL", response.target_code);
        Assertions.assertEquals(5.25, response.conversion_rate, 0.001);
    }

    @Test
    void parseFullResponse_errorResult_throwsException() {
        String jsonResponse = """
            {
                "result": "error",
                "error-type": "unsupported-code"
            }
            """;

        JsonResponseParser parser = new JsonResponseParser();
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parser.parseFullResponse(jsonResponse);
        });
        
        Assertions.assertTrue(exception.getMessage().contains("unsupported-code"));
    }

    @Test
    void extractField_validJson_returnsFieldValue() {
        String jsonResponse = """
            {
                "result": "success",
                "base_code": "EUR",
                "target_code": "JPY"
            }
            """;

        JsonResponseParser parser = new JsonResponseParser();
        String baseCode = parser.extractField(jsonResponse, "base_code");
        String targetCode = parser.extractField(jsonResponse, "target_code");
        
        Assertions.assertEquals("EUR", baseCode);
        Assertions.assertEquals("JPY", targetCode);
    }

    @Test
    void manualJsonParsing_demonstration() {
        // Demonstração da etapa_7
        String resposta = """
            {
                "result": "success",
                "conversion_rate": 0.92
            }
            """;

        // Conversão para JSON usando JsonParser
        var elemento = JsonParser.parseString(resposta);
        JsonObject objectRoot = elemento.getAsJsonObject();
        
        // Acessando o JsonObject
        double taxa = objectRoot.get("conversion_rate").getAsDouble();
        
        Assertions.assertEquals(0.92, taxa, 0.001);
    }
}
