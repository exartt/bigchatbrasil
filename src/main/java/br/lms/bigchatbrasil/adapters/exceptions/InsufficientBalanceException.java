package br.lms.bigchatbrasil.adapters.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Não há saldo suficiente para enviar a mensagem.");
    }
}