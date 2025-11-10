package com.otto.conversormoedas.client;

/** Resposta JSON do endpoint /pair da ExchangeRate-API (v6). */
public class ExchangePairResponse {
    public String result;
    public String documentation;
    public String terms_of_use;
    public long time_last_update_unix;
    public String time_last_update_utc;
    public long time_next_update_unix;
    public String time_next_update_utc;
    public String base_code;
    public String target_code;
    public double conversion_rate;
    public double conversion_result; // sometimes provided by the API

    /** Representação resumida. */
    @Override
    public String toString() {
        return "ExchangePairResponse{" +
                "result='" + result + '\'' +
                ", base='" + base_code + '\'' +
                ", target='" + target_code + '\'' +
                ", rate=" + conversion_rate +
                '}';
    }
}
