package com.otto.conversormoedas;

import com.otto.conversormoedas.api.ApiServer;
import com.otto.conversormoedas.client.ExchangeRateClient;
import com.otto.conversormoedas.service.CurrencyConverterService;
import com.otto.conversormoedas.ui.ConsoleUI;

/**
 * Aplicação principal do Conversor de Moedas.
 * Oracle ONE - Desafio Java.
 * 
 * Modos de execução:
 * - Console (padrão): java -jar conversor-moedas.jar
 * - API Server: java -jar conversor-moedas.jar --server
 * - API Server (porta customizada): java -jar conversor-moedas.jar --server --port=8080
 */
public class App {
    private static final int DEFAULT_PORT = 7000;

    /**
     * Ponto de entrada da aplicação.
     *
     * Comportamento:
     * - Inicializa o cliente de câmbio e o serviço de conversões.
     * - Decide o modo de execução com base nos argumentos/variáveis de ambiente:
     *   - Modo Console (padrão): exibe menu interativo no terminal.
     *   - Modo Servidor (--server ou SERVER_MODE=true): inicia a API HTTP (Javalin).
     *
     * Tratamento de erros:
     * - IllegalStateException: problema de configuração (ex.: API key ausente).
     * - Exception genérica: qualquer erro inesperado é logado e encerra o processo.
     */
    public static void main(String[] args) {
        try {
            // Inicializa o client e o service
            ExchangeRateClient client = new ExchangeRateClient();
            CurrencyConverterService service = new CurrencyConverterService(client);
            
            // Detecta modo de execução
            boolean serverMode = hasArg(args, "--server") || hasEnvVar("SERVER_MODE");
            
            if (serverMode) {
                // Modo API Server (para deploy)
                int port = getPort(args);
                ApiServer server = new ApiServer(service, port);
                server.start();
                
                // Mantém o servidor rodando
                Thread.currentThread().join();
            } else {
                // Modo Console (padrão)
                ConsoleUI ui = new ConsoleUI(service);
                ui.start();
            }
            
        } catch (IllegalStateException e) {
            System.err.println("❌ Erro de configuração: " + e.getMessage());
            System.err.println("\n💡 Dica: Configure a variável de ambiente EXCHANGE_RATE_API_KEY");
            System.err.println("   Obtenha sua chave em: https://www.exchangerate-api.com/");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Verifica se um argumento específico foi passado na linha de comando.
     *
     * Exemplo: hasArg(["--server", "--port=8080"], "--server") ➜ true
     *
     * @param args argumentos recebidos no método main
     * @param arg  argumento a procurar (comparação exata)
     * @return true se o argumento existir, false caso contrário
     */
    private static boolean hasArg(String[] args, String arg) {
        for (String a : args) {
            if (a.equals(arg)) return true;
        }
        return false;
    }

    /**
     * Verifica se uma variável de ambiente booleana está habilitada.
     * Considera como "verdadeiro" os valores: "true" (case-insensitive) ou "1".
     *
     * Exemplo: SERVER_MODE=true ➜ hasEnvVar("SERVER_MODE") == true
     *
     * @param name nome da variável de ambiente
     * @return true se definida como valor verdadeiro, false caso contrário
     */
    private static boolean hasEnvVar(String name) {
        String value = System.getenv(name);
        return value != null && (value.equalsIgnoreCase("true") || value.equals("1"));
    }

    /**
     * Obtém a porta HTTP para o servidor da API.
     * Ordem de precedência:
     * 1) Variável de ambiente PORT (usada por plataformas como Render/Railway)
     * 2) Argumento de execução no formato --port=<numero>
     * 3) Porta padrão (DEFAULT_PORT)
     *
     * Exemplos de uso:
     * - PORT=8080 ➜ retorna 8080
     * - args: ["--server", "--port=9090"] ➜ retorna 9090
     * - nenhum fornecido ➜ retorna 7000
     *
     * @param args argumentos recebidos no método main
     * @return porta numérica válida para o servidor HTTP
     */
    private static int getPort(String[] args) {
        // Verifica env var PORT primeiro (Railway/Render usam isso)
        String portEnv = System.getenv("PORT");
        if (portEnv != null) {
            try {
                return Integer.parseInt(portEnv);
            } catch (NumberFormatException ignored) {}
        }

        // Verifica argumento --port=8080
        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                try {
                    return Integer.parseInt(arg.substring(7));
                } catch (NumberFormatException ignored) {}
            }
        }

        return DEFAULT_PORT;
    }
}
