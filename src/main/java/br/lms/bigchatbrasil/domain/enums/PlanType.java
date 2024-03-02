package br.lms.bigchatbrasil.domain.enums;

public enum PlanType {
    PREPAID("PREPAID"),
    POSTPAID("POSTPAID");

    private final String planName;

    PlanType(String planName) {
        this.planName = planName;
    }

    public String getPlanName() {
        return switch (this) {
            case PREPAID -> "pré-pago";
            case POSTPAID -> "pós-pago";
            default -> throw new IllegalStateException("Tipo de plano desconhecido: " + this);
        };
    }

    @Override
    public String toString() {
        return this.planName;
    }
}