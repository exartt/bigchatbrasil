package br.lms.bigchatbrasil.adapters.exceptions;

public class InvalidPlanTypeException extends RuntimeException {
    public InvalidPlanTypeException(String clientPlan) {
        super("Ação não permitida: o plano atual do cliente (" + clientPlan + ") não suporta esta operação.");
    }
}