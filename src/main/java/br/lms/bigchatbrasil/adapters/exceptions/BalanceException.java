package br.lms.bigchatbrasil.adapters.exceptions;

public class BalanceException extends RuntimeException {
    public BalanceException() {
        super("Não há saldo suficiente para enviar a mensagem.");
    }
}