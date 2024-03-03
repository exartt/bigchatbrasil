package br.lms.bigchatbrasil.domain.service;

import br.lms.bigchatbrasil.adapters.dto.ClientDTO;
import br.lms.bigchatbrasil.adapters.dto.TypePlanDTO;
import br.lms.bigchatbrasil.domain.model.Client;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public interface IClientService {
    Client getClientReferenceById(long clientId);
    ClientDTO getClientInformation (long clientId);
    BigDecimal getClientBalance (long clientId);
    BigDecimal getAccountBalanceByClientId (long clientId);
    CompletableFuture<Void> updateTypePlan (long clientId, TypePlanDTO typePlanDTO);
}
