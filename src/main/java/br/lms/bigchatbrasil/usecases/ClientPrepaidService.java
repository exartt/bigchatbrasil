package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPrepaid;
import br.lms.bigchatbrasil.domain.service.IClientMessageService;
import br.lms.bigchatbrasil.domain.service.IClientPrepaidService;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientPrepaidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientPrepaidService implements IClientPrepaidService {
    @Autowired
    private ClientPrepaidRepository clientPrepaidRepository;

    @Override
    public void insertCreditByClient (Client client, float credit) {
        ClientPrepaid clientPrepaid = client.getClientPrepaid();
        clientPrepaid.setAmountCredit(clientPrepaid.getAmountCredit() + credit);
    }
}
