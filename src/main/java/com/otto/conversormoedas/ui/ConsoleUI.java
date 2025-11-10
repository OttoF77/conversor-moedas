package com.otto.conversormoedas.ui;

import com.otto.conversormoedas.model.CurrencyCode;
import com.otto.conversormoedas.service.CurrencyConverterService;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface de console para conversões.
 * Menu com opções fixas + personalizada.
 */
public class ConsoleUI {
    private static final Logger logger = Logger.getLogger(ConsoleUI.class.getName());
    private final CurrencyConverterService service;
    private final Scanner scanner;

    public ConsoleUI(CurrencyConverterService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    /** Inicia loop principal do menu. */
    public void start() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   CONVERSOR DE MOEDAS - Oracle ONE     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();

        boolean running = true;
        while (running) {
            running = showMenuAndProcessChoice();
        }

        scanner.close();
        System.out.println("\n👋 Obrigado por usar o Conversor de Moedas!");
    }

    /** Exibe menu e processa escolha. */
    private boolean showMenuAndProcessChoice() {
        printMenu();
        String choice = scanner.nextLine().trim();
        System.out.println();

        try {
            switch (choice) {
                case "1":
                    convertCurrency("BRL", "USD");
                    break;
                case "2":
                    convertCurrency("USD", "BRL");
                    break;
                case "3":
                    convertCurrency("BRL", "ARS");
                    break;
                case "4":
                    convertCurrency("USD", "COP");
                    break;
                case "5":
                    convertCurrency("BRL", "CLP");
                    break;
                case "6":
                    convertCurrency("USD", "BOB");
                    break;
                case "7":
                    customConversion();
                    break;
                case "8":
                    listSupportedCurrencies();
                    break;
                case "9":
                    return false; // Sair
                default:
                    System.out.println("❌ Opção inválida. Escolha um número de 1 a 9.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Erro ao processar conversão", e);
            System.out.println("❌ Erro: " + e.getMessage());
        }

        System.out.println();
        return true;
    }

    /** Imprime o menu. */
    private void printMenu() {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│          MENU DE CONVERSÕES            │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. BRL → USD (Real → Dólar)            │");
        System.out.println("│ 2. USD → BRL (Dólar → Real)            │");
        System.out.println("│ 3. BRL → ARS (Real → Peso argentino)   │");
        System.out.println("│ 4. USD → COP (Dólar → Peso colombiano) │");
        System.out.println("│ 5. BRL → CLP (Real → Peso chileno)     │");
        System.out.println("│ 6. USD → BOB (Dólar → Boliviano)       │");
        System.out.println("│ 7. 🔧 Conversão personalizada          │");
        System.out.println("│ 8. 📋 Listar moedas suportadas         │");
        System.out.println("│ 9. 🚪 Sair                              │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    /** Converte entre duas moedas específicas. */
    private void convertCurrency(String from, String to) {
        System.out.printf("💱 Conversão: %s → %s%n", from, to);
        System.out.print("Digite o valor a converter: ");
        
        String input = scanner.nextLine().trim();
        
        try {
            double amount = parseAmount(input);
            if (amount <= 0) {
                System.out.println("❌ O valor deve ser positivo.");
                return;
            }

            System.out.println("⏳ Consultando taxa de câmbio...");
            double result = service.convert(amount, from, to);
            double rate = service.getRate(from, to);

            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.printf("✅ Taxa: 1 %s = %.6f %s%n", from, rate, to);
            System.out.printf("💰 Resultado: %.2f %s = %.2f %s%n", amount, from, result, to);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        } catch (NumberFormatException e) {
            System.out.println("❌ Valor inválido. Use números com ponto ou vírgula como separador decimal.");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Erro na conversão", e);
            System.out.println("❌ Erro ao realizar conversão: " + e.getMessage());
        }
    }

    /** Conversão personalizada (qualquer par). */
    private void customConversion() {
        System.out.println("🔧 CONVERSÃO PERSONALIZADA");
        System.out.println("Moedas disponíveis: " + String.join(", ", getSupportedCodes()));
        
        System.out.print("Moeda de origem: ");
        String from = scanner.nextLine().trim().toUpperCase();
        
        if (!CurrencyCode.isValid(from)) {
            System.out.println("❌ Moeda de origem inválida: " + from);
            return;
        }
        
        System.out.print("Moeda de destino: ");
        String to = scanner.nextLine().trim().toUpperCase();
        
        if (!CurrencyCode.isValid(to)) {
            System.out.println("❌ Moeda de destino inválida: " + to);
            return;
        }
        
        if (from.equals(to)) {
            System.out.println("❌ As moedas de origem e destino devem ser diferentes.");
            return;
        }
        
        convertCurrency(from, to);
    }

    /** Lista moedas suportadas. */
    private void listSupportedCurrencies() {
        System.out.println("📋 MOEDAS SUPORTADAS:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        for (CurrencyCode currency : CurrencyCode.values()) {
            System.out.printf("  • %s - %s%n", currency.name(), currency.getDescription());
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /** Retorna array de códigos suportados. */
    private String[] getSupportedCodes() {
        CurrencyCode[] currencies = CurrencyCode.values();
        String[] codes = new String[currencies.length];
        for (int i = 0; i < currencies.length; i++) {
            codes[i] = currencies[i].name();
        }
        return codes;
    }

    /** Faz parsing do valor (vírgula ou ponto). */
    private double parseAmount(String input) throws NumberFormatException {
        // Aceita tanto vírgula quanto ponto como separador decimal
        String normalized = input.replace(',', '.');
        return Double.parseDouble(normalized);
    }
}
