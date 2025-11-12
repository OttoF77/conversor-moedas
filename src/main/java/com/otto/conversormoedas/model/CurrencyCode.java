package com.otto.conversormoedas.model;

/** Enum de moedas suportadas. */
public enum CurrencyCode {
    // Moedas da América do Sul
    ARS("Peso argentino"),
    BOB("Boliviano boliviano"),
    BRL("Real brasileiro"),
    CLP("Peso chileno"),
    COP("Peso colombiano"),
    PEN("Sol peruano"),
    UYU("Peso uruguaio"),
    PYG("Guarani paraguaio"),
    
    // Moedas da América do Norte
    USD("Dólar americano"),
    CAD("Dólar canadense"),
    MXN("Peso mexicano"),
    
    // Moedas Europeias
    EUR("Euro"),
    GBP("Libra esterlina"),
    CHF("Franco suíço"),
    
    // Moedas Asiáticas
    JPY("Iene japonês"),
    CNY("Yuan chinês"),
    INR("Rúpia indiana"),
    KRW("Won sul-coreano"),
    
    // Moedas da Oceania
    AUD("Dólar australiano"),
    NZD("Dólar neozelandês");

    private final String description;

    CurrencyCode(String description) {
        this.description = description;
    }

    /** Descrição legível. */
    public String getDescription() {
        return description;
    }

    /** Valida se o código informado existe. */
    public static boolean isValid(String code) {
        if (code == null || code.isBlank()) {
            return false;
        }
        try {
            valueOf(code.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /** Converte string em enum (case-insensitive). */
    public static CurrencyCode fromString(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Código de moeda não pode ser vazio");
        }
        return valueOf(code.toUpperCase());
    }

    /** Representação amigável. */
    @Override
    public String toString() {
        return name() + " - " + description;
    }
}
