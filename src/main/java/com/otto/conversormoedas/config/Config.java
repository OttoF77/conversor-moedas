package com.otto.conversormoedas.config;

public final class Config {
    private Config() {}

    /**
     * Lê a API Key (EXCHANGE_RATE_API_KEY).
     * Retorna a chave ou lança IllegalStateException com mensagem didática.
     */
    public static String getExchangeRateApiKey() {
        String key = System.getenv("EXCHANGE_RATE_API_KEY");
        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                "⚠️  API Key não configurada!\n" +
                "\n" +
                "Para desenvolvedores:\n" +
                "  Configure a variável de ambiente EXCHANGE_RATE_API_KEY\n" +
                "  Consulte o README.md para instruções detalhadas.\n" +
                "\n" +
                "Para usuários do serviço web:\n" +
                "  Entre em contato com o administrador do sistema.\n" +
                "  A conversão de moedas está temporariamente indisponível."
            );
        }
        return key;
    }
    
    /** Verifica se a API Key está configurada (sem expor valor). */
    public static boolean isApiKeyConfigured() {
        String key = System.getenv("EXCHANGE_RATE_API_KEY");
        return key != null && !key.isBlank();
    }
}
