package com.otto.conversormoedas.api;

import com.otto.conversormoedas.model.CurrencyCode;
import com.otto.conversormoedas.service.CurrencyConverterService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servidor REST com Javalin.
 * Expõe endpoints HTTP para conversão de moedas.
 */
public class ApiServer {
    private final CurrencyConverterService service;
    private final Javalin app;
    private final int port;

    public ApiServer(CurrencyConverterService service, int port) {
        this.service = service;
        this.port = port;
        this.app = createApp();
    }

    /**
     * Cria e configura a instância do Javalin.
     * - CORS liberado (dev e GitHub Pages)
     * - Arquivos estáticos opcionais (frontend separado)
     */
    private Javalin createApp() {
        return Javalin.create(config -> {
            // Habilita CORS (qualquer origem)
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.anyHost();
                });
            });
            
            // Arquivos estáticos apenas se existirem (opcional)
            // Frontend está em repositório separado (GitHub Pages)
            try {
                if (getClass().getResourceAsStream("/public/index.html") != null) {
                    config.staticFiles.add("/public");
                }
            } catch (Exception e) {
                // Ignora se /public não existir - normal em deploy de backend
            }
        });
    }

    /**
     * Configura rotas e inicia o servidor.
     */
    public void start() {
        app.get("/", this::handleRoot);
        app.get("/health", this::handleHealth);
        app.get("/api/convert", this::handleConvert);
        app.get("/api/rates", this::handleRates);
        app.get("/api/currencies", this::handleCurrencies);
        
            app.start("0.0.0.0", port);
        System.out.println("🚀 API Server rodando em http://localhost:" + port);
        System.out.println("📚 Endpoints disponíveis:");
        System.out.println("   GET /api/convert?from=USD&to=BRL&amount=100");
        System.out.println("   GET /api/rates?from=USD");
        System.out.println("   GET /api/currencies");
        System.out.println("   GET /health");
    }

    /**
     * Para o servidor.
     */
    public void stop() {
        app.stop();
    }

    /**
     * Raiz da API – informações básicas.
     */
    private void handleRoot(Context ctx) {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Conversor de Moedas API");
        response.put("version", "0.1.0");
        response.put("endpoints", Arrays.asList(
            "/api/convert?from=USD&to=BRL&amount=100",
            "/api/rates?from=USD",
            "/api/currencies",
            "/health"
        ));
        ctx.json(response);
    }

    /**
     * Verificação de saúde (usado por Render).
     */
    private void handleHealth(Context ctx) {
        Map<String, String> health = new HashMap<>();
        health.put("status", "healthy");
        health.put("service", "conversor-moedas");
        ctx.json(health);
    }

    /**
     * Converte moeda.
     * GET /api/convert?from=USD&to=BRL&amount=100
     */
    private void handleConvert(Context ctx) {
        try {
            String fromParam = ctx.queryParam("from");
            String toParam = ctx.queryParam("to");
            String amountParam = ctx.queryParam("amount");

            // Valida parâmetros obrigatórios
            if (fromParam == null || toParam == null || amountParam == null) {
                ctx.status(400).json(Map.of(
                    "error", "Parâmetros obrigatórios ausentes",
                    "required", "from, to, amount",
                    "example", "/api/convert?from=USD&to=BRL&amount=100"
                ));
                return;
            }

            // Faz parse/validação das moedas
            CurrencyCode from = CurrencyCode.fromString(fromParam);
            CurrencyCode to = CurrencyCode.fromString(toParam);
            
            if (from == null || to == null) {
                ctx.status(400).json(Map.of(
                    "error", "Código de moeda inválido",
                    "supported", Arrays.stream(CurrencyCode.values())
                        .map(CurrencyCode::name)
                        .collect(Collectors.toList())
                ));
                return;
            }

            // Faz parse do amount
            double amount;
            try {
                amount = Double.parseDouble(amountParam.replace(",", "."));
                if (amount <= 0) {
                    throw new NumberFormatException("Valor deve ser positivo");
                }
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of(
                    "error", "Valor inválido",
                    "message", "O valor deve ser um número positivo",
                    "example", "100 or 100.50"
                ));
                return;
            }

            // Executa conversão
            var result = service.convertDetailed(from, to, amount);

            // Monta resposta JSON
            Map<String, Object> response = new HashMap<>();
            response.put("from", from.name());
            response.put("to", to.name());
            response.put("amount", amount);
            response.put("result", result.getConvertedAmount());
            response.put("rate", result.getExchangeRate());
            response.put("timestamp", System.currentTimeMillis());

            ctx.json(response);

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "error", "Falha na conversão",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Retorna as taxas para a moeda base.
     * GET /api/rates?from=USD
     */
    private void handleRates(Context ctx) {
        try {
            String fromParam = ctx.queryParam("from");

            if (fromParam == null) {
                ctx.status(400).json(Map.of(
                    "error", "Parâmetro obrigatório 'from' ausente",
                    "example", "/api/rates?from=USD"
                ));
                return;
            }

            CurrencyCode from = CurrencyCode.fromString(fromParam);
            if (from == null) {
                ctx.status(400).json(Map.of(
                    "error", "Código de moeda inválido",
                    "supported", Arrays.stream(CurrencyCode.values())
                        .map(CurrencyCode::name)
                        .collect(Collectors.toList())
                ));
                return;
            }

            // Monta mapa de taxas para as moedas suportadas
            Map<String, Double> rates = new HashMap<>();
            for (CurrencyCode to : CurrencyCode.values()) {
                if (from != to) {
                    double rate = service.getRate(from, to);
                    rates.put(to.name(), rate);
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("base", from.name());
            response.put("rates", rates);
            response.put("timestamp", System.currentTimeMillis());

            ctx.json(response);

        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                "error", "Falha ao buscar taxas",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Lista moedas suportadas.
     * GET /api/currencies
     */
    private void handleCurrencies(Context ctx) {
        var currencies = Arrays.stream(CurrencyCode.values())
            .map(code -> Map.of(
                "code", code.name(),
                "description", code.toString()
            ))
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("currencies", currencies);
        response.put("count", currencies.size());

        ctx.json(response);
    }
}
