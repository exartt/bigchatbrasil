package br.lms.bigchatbrasil.domain.enums;

public enum PlanType {
    PREPAID,
    POSTPAID;

    public static PlanType fromOrdinal(int ordinal) {
        if (ordinal >= 0 && ordinal < values().length) {
            return values()[ordinal];
        }
        throw new IllegalArgumentException("Valor ordinal inválido para o tipo de plano: " + ordinal);
    }
}
