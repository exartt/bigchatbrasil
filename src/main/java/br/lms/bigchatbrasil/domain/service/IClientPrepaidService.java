package br.lms.bigchatbrasil.domain.service;

import br.lms.bigchatbrasil.domain.model.Client;

public interface IClientPrepaidService {
    void insertCreditByClient (Client client, float credit);

}
