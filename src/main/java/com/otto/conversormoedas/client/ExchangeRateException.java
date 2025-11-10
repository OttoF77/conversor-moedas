package com.otto.conversormoedas.client;

/**
 * Exceção para erros da ExchangeRate-API
 * ou respostas inválidas do cliente HTTP.
 */
public class ExchangeRateException extends Exception {
    /** Mensagem do erro. */
    public ExchangeRateException(String message) {
        super(message);
    }

    /** Mensagem e causa raiz. */
    public ExchangeRateException(String message, Throwable cause) {
        super(message, cause);
    }
}
