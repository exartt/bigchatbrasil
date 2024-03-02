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
        };
    }

    public static PlanType fromString(String planName) {
        for (PlanType type : PlanType.values()) {
            if (type.planName.equalsIgnoreCase(planName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de plano inválido: " + planName);
    }

    @Override
    public String toString() {
        return this.planName;
    }
}