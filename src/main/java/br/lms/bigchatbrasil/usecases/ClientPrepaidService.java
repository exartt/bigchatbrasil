package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidPlanTypeException;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPrepaid;
import br.lms.bigchatbrasil.domain.service.IClientPrepaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

@Service
public class ClientPrepaidService implements IClientPrepaidService {
    private static final Logger logger = LoggerFactory.getLogger(ClientPrepaidService.class);
    private final IClientService clientService;
    private final BigDecimal MESSAGE_FEE;
    @Autowired
    public ClientPrepaidService(IClientService clientService,
                                @Value("${message.fee}") String messageFee) {
        this.clientService = clientService;
        this.MESSAGE_FEE = new BigDecimal(messageFee);
    }

    private void insertCreditByClient (Client client, float credit) {
        ClientPrepaid clientPrepaid = client.getClientPrepaid();
        float newTotalCredit = clientPrepaid.getAmountCredit() + credit;
        clientPrepaid.setAmountCredit(newTotalCredit);
        logger.info("Adding credit: {} to client ID: {}, new total credit: {}", credit, client.getId(), newTotalCredit);
    }
    @Async
    @Override
    @Transactional
    public CompletableFuture<Float> insertCredit (CreditDTO creditDTO) {
        logger.info("Initiating credit insertion for client ID: {}", creditDTO.getClientId());
        Client client = clientService.getClientReferenceById(creditDTO.getClientId());
        if(client.getPlanType() == PlanType.PREPAID) {
            this.insertCreditByClient(client, creditDTO.getAmountCredit());
        } else {
            logger.warn("Invalid plan type: {} for client ID: {}", client.getPlanType(), client.getId());
            throw new InvalidPlanTypeException(client.getPlanType().toString());
        }
        return CompletableFuture.completedFuture(client.getClientPrepaid().getAmountCredit());
    }

    @Override
    @Transactional(noRollbackFor = com.twilio.exception.ApiException.class)
    public void deduceCredit (Client client) {
        BigDecimal amountCredit = BigDecimal.valueOf(client.getClientPrepaid().getAmountCredit());
        float deducedCredit = amountCredit.subtract(MESSAGE_FEE).setScale(2, RoundingMode.HALF_EVEN).floatValue();
        client.getClientPrepaid().setAmountCredit(deducedCredit);
        logger.info("Deducting credit for client ID: {}, amount deducted: {}, new total credit: {}", client.getId(), MESSAGE_FEE, deducedCredit);
    }
}
