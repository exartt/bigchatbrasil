package br.lms.bigchatbrasil.domain.service;

import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.domain.model.Client;

import java.util.concurrent.CompletableFuture;

public interface IClientPostpaidService {
    CompletableFuture<Void> updateLimitAmount(long clientId, PostpaidDTO postpaidDTO);
    void increaseSpentValue(Client client);
}
