package br.lms.bigchatbrasil.domain.service;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.domain.model.Client;

public interface IClientService {
    Client getClientById (long clientId);
    void insertCredit(CreditDTO creditDTO);
    void updateLimitAmount(long clientId, PostpaidDTO postpaidDTO);
}
