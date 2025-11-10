package com.otto.conversormoedas.model;

/** Enum de moedas suportadas. */
public enum CurrencyCode {
    ARS("Peso argentino"),
    BOB("Boliviano boliviano"),
    BRL("Real brasileiro"),
    CLP("Peso chileno"),
    COP("Peso colombiano"),
    USD("Dólar americano");

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
