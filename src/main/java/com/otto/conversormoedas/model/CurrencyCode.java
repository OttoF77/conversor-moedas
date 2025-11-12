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
    VES("Bolívar venezuelano"),
    GYD("Dólar guianense"),
    SRD("Dólar surinamês"),
    
    // Moedas da América do Norte
    USD("Dólar americano"),
    CAD("Dólar canadense"),
    MXN("Peso mexicano"),
    
    // Moedas da América Central e Caribe
    CRC("Colón costarriquenho"),
    GTQ("Quetzal guatemalteco"),
    HNL("Lempira hondurenha"),
    NIO("Córdoba nicaraguense"),
    PAB("Balboa panamenho"),
    DOP("Peso dominicano"),
    HTG("Gourde haitiano"),
    JMD("Dólar jamaicano"),
    TTD("Dólar trinitário"),
    BBD("Dólar barbadiano"),
    
    // Moedas Europeias
    EUR("Euro"),
    GBP("Libra esterlina"),
    CHF("Franco suíço"),
    NOK("Coroa norueguesa"),
    SEK("Coroa sueca"),
    DKK("Coroa dinamarquesa"),
    ISK("Coroa islandesa"),
    PLN("Zloty polonês"),
    CZK("Coroa tcheca"),
    HUF("Forint húngaro"),
    RON("Leu romeno"),
    BGN("Lev búlgaro"),
    HRK("Kuna croata"),
    RUB("Rublo russo"),
    UAH("Hryvnia ucraniana"),
    TRY("Lira turca"),
    
    // Moedas Asiáticas
    JPY("Iene japonês"),
    CNY("Yuan chinês"),
    HKD("Dólar de Hong Kong"),
    TWD("Dólar taiwanês"),
    KRW("Won sul-coreano"),
    SGD("Dólar singapuriano"),
    MYR("Ringgit malaio"),
    THB("Baht tailandês"),
    IDR("Rupia indonésia"),
    PHP("Peso filipino"),
    VND("Dong vietnamita"),
    INR("Rúpia indiana"),
    PKR("Rúpia paquistanesa"),
    BDT("Taka bengali"),
    LKR("Rúpia do Sri Lanka"),
    NPR("Rúpia nepalesa"),
    
    // Moedas do Oriente Médio
    AED("Dirham dos Emirados"),
    SAR("Riyal saudita"),
    QAR("Riyal catariano"),
    KWD("Dinar kuwaitiano"),
    BHD("Dinar bareinita"),
    OMR("Rial omanense"),
    JOD("Dinar jordaniano"),
    ILS("Novo shekel israelense"),
    
    // Moedas Africanas
    ZAR("Rand sul-africano"),
    EGP("Libra egípcia"),
    NGN("Naira nigeriana"),
    KES("Xelim queniano"),
    GHS("Cedi ganês"),
    TZS("Xelim tanzaniano"),
    UGX("Xelim ugandense"),
    MAD("Dirham marroquino"),
    TND("Dinar tunisiano"),
    
    // Moedas da Oceania
    AUD("Dólar australiano"),
    NZD("Dólar neozelandês"),
    FJD("Dólar fijiano"),
    PGK("Kina papuásia"),
    
    // Outras Moedas Importantes
    XAU("Ouro (onça troy)"),
    XAG("Prata (onça troy)");

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
