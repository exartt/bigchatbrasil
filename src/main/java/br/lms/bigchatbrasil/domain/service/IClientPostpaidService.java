package br.lms.bigchatbrasil.domain.service;

import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.domain.model.Client;

public interface IClientPostpaidService {
    void updateLimitAmount(long clientId, PostpaidDTO postpaidDTO);
    void increaseSpentValue(Client client);
}
