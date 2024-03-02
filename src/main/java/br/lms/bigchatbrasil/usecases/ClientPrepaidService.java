package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidPlanTypeException;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPrepaid;
import br.lms.bigchatbrasil.domain.service.IClientPrepaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientPrepaidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ClientPrepaidService implements IClientPrepaidService {
    private final ClientPrepaidRepository clientPrepaidRepository;
    private final IClientService clientService;
    private final BigDecimal MESSAGE_FEE;
    @Autowired
    public ClientPrepaidService(ClientPrepaidRepository clientPrepaidRepository,
                                IClientService clientService,
                                @Value("${message.fee}") String messageFee) {
        this.clientPrepaidRepository = clientPrepaidRepository;
        this.clientService = clientService;
        this.MESSAGE_FEE = new BigDecimal(messageFee);
    }

    private void insertCreditByClient (Client client, float credit) {
        ClientPrepaid clientPrepaid = client.getClientPrepaid();
        clientPrepaid.setAmountCredit(clientPrepaid.getAmountCredit() + credit);
    }

    @Override
    @Transactional
    public void insertCredit (CreditDTO creditDTO) {
        Client client = clientService.getClientById(creditDTO.getClientId());
        if(client.getPlanType() == PlanType.PREPAID) {
            this.insertCreditByClient(client, creditDTO.getAmountCredit());
        } else {
            throw new InvalidPlanTypeException(client.getPlanType().toString());
        }
    }

    public void deduceCredit (Client client) {
        BigDecimal amountCredit = BigDecimal.valueOf(client.getClientPrepaid().getAmountCredit());
        float deducedCredit = amountCredit.subtract(MESSAGE_FEE).setScale(2, RoundingMode.HALF_EVEN).floatValue();
        client.getClientPrepaid().setAmountCredit(deducedCredit);
    }
}
