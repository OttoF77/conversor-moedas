package com.otto.conversormoedas.util;

/** Utilitário de cálculos de conversão. */
public class ConversionCalculator {

    /** Converte usando taxa (valor * taxa). */
    public static double convert(double amount, double exchangeRate) {
        validatePositive(amount, "amount");
        validatePositive(exchangeRate, "exchangeRate");
        
        return amount * exchangeRate;
    }

    /** Obtém valor original a partir do convertido. */
    public static double reverseConversion(double convertedAmount, double exchangeRate) {
        validatePositive(convertedAmount, "convertedAmount");
        validatePositive(exchangeRate, "exchangeRate");
        
        if (exchangeRate == 0) {
            throw new IllegalArgumentException("Taxa de câmbio não pode ser zero");
        }
        
        return convertedAmount / exchangeRate;
    }

    /** Diferença percentual (ganho/perda). */
    public static double calculatePercentageDifference(double originalValue, double convertedValue) {
        validatePositive(originalValue, "originalValue");
        
        if (originalValue == 0) {
            return 0;
        }
        
        return ((convertedValue - originalValue) / originalValue) * 100;
    }

    /** Taxa inversa (1 / taxa). */
    public static double getInverseRate(double exchangeRate) {
        validatePositive(exchangeRate, "exchangeRate");
        
        if (exchangeRate == 0) {
            throw new IllegalArgumentException("Taxa de câmbio não pode ser zero");
        }
        
        return 1.0 / exchangeRate;
    }

    /** Arredonda para N casas decimais. */
    public static double round(double value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Casas decimais não pode ser negativo");
        }
        
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(value * multiplier) / multiplier;
    }

    /** Verifica se valor é positivo. */
    private static void validatePositive(double value, String paramName) {
        if (value < 0) {
            throw new IllegalArgumentException(paramName + " não pode ser negativo: " + value);
        }
    }

    /** Resultado detalhado de conversão. */
    public static class ConversionResult {
        private final double originalAmount;
        private final String fromCurrency;
        private final double convertedAmount;
        private final String toCurrency;
        private final double exchangeRate;
        private final double inverseRate;

        /** Construtor principal. */
        public ConversionResult(double originalAmount, String fromCurrency,
                                double convertedAmount, String toCurrency,
                                double exchangeRate) {
            this.originalAmount = originalAmount;
            this.fromCurrency = fromCurrency;
            this.convertedAmount = convertedAmount;
            this.toCurrency = toCurrency;
            this.exchangeRate = exchangeRate;
            this.inverseRate = ConversionCalculator.getInverseRate(exchangeRate);
        }

        /** Valor original. */
        public double getOriginalAmount() {
            return originalAmount;
        }

        /** Moeda origem. */
        public String getFromCurrency() {
            return fromCurrency;
        }

        /** Valor convertido. */
        public double getConvertedAmount() {
            return convertedAmount;
        }

        /** Moeda destino. */
        public String getToCurrency() {
            return toCurrency;
        }

        /** Taxa usada. */
        public double getExchangeRate() {
            return exchangeRate;
        }

        /** Taxa inversa. */
        public double getInverseRate() {
            return inverseRate;
        }

        /** String amigável. */
        @Override
        public String toString() {
            return String.format("%.2f %s = %.2f %s (taxa: %.6f)", 
                originalAmount, fromCurrency, convertedAmount, toCurrency, exchangeRate);
        }
    }
}
