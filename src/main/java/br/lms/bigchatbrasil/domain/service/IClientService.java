package br.lms.bigchatbrasil.domain.service;

import br.lms.bigchatbrasil.adapters.dto.TypePlanDTO;
import br.lms.bigchatbrasil.domain.model.Client;

import java.math.BigDecimal;

public interface IClientService {
    Client getClientById (long clientId);
    BigDecimal getAccountBalanceByClientId (long clientId);
    void updateTypePlan (long clientId, TypePlanDTO typePlanDTO);
}
