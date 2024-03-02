package br.lms.bigchatbrasil.domain.service;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.domain.model.Client;

public interface IClientPrepaidService {
    void insertCredit (CreditDTO creditDTO);
    void deduceCredit (Client client);
}
