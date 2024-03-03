package br.lms.bigchatbrasil.domain.service;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.domain.model.Client;

import java.util.concurrent.CompletableFuture;

public interface IClientPrepaidService {
    CompletableFuture<Void> insertCredit (CreditDTO creditDTO);
    void deduceCredit (Client client);
}
