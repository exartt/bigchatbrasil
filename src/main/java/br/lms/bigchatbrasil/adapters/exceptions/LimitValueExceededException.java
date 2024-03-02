package br.lms.bigchatbrasil.adapters.exceptions;

public class LimitValueExceededException extends RuntimeException {
    public LimitValueExceededException(float newAmount, float spentAmount) {
        super("Operação inválida: o novo limite de " + newAmount + " excede o total gasto atual de " + spentAmount + ".");
    }
}