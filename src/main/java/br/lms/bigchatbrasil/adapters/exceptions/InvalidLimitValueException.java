package br.lms.bigchatbrasil.adapters.exceptions;

public class InvalidLimitValueException extends RuntimeException {
    public InvalidLimitValueException() {
        super("Valor limite inválido: o valor deve ser maior que zero.");
    }
}