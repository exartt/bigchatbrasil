package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidLimitValueException;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidPlanTypeException;
import br.lms.bigchatbrasil.adapters.exceptions.LimitValueExceededException;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.service.IClientPostpaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class ClientPostpaidService implements IClientPostpaidService {
    private static final Logger logger = LoggerFactory.getLogger(ClientPostpaidService.class);
    private final IClientService clientService;
    private final BigDecimal MESSAGE_FEE;
    @Autowired
    public ClientPostpaidService(IClientService clientService,
                                 @Value("${message.fee}") String messageFee) {
        this.clientService = clientService;
        this.MESSAGE_FEE = new BigDecimal(messageFee);
    }

    @Async
    @Override
    @Transactional
    public CompletableFuture<Void> updateLimitAmount(long clientId, PostpaidDTO postpaidDTO) {
        logger.info("Starting updateLimitAmount for client ID: {}", clientId);
        try {
            Client client = clientService.getClientReferenceById(clientId);
            validateClientForPostpaidUpdate(client, postpaidDTO);
            updateClientPostpaidData(client, postpaidDTO);
            logger.info("Completed updateLimitAmount for client ID: {}", clientId);
        } catch (Exception e) {
            logger.error("Error in updateLimitAmount for client ID: {}: {}", clientId, e.getMessage(), e);
            throw e;
        }
        return CompletableFuture.completedFuture(null);
    }
    @Override
    @Transactional(noRollbackFor = com.twilio.exception.ApiException.class)
    public void increaseSpentValue(Client client) {
        BigDecimal oldBalance = BigDecimal.valueOf(client.getClientPostpaid().getSpentValue());
        BigDecimal newBalance = oldBalance.add(MESSAGE_FEE);
        client.getClientPostpaid().setSpentValue(newBalance.floatValue());
    }

    private void validateClientForPostpaidUpdate(Client client, PostpaidDTO postpaidDTO) {
        logger.debug("Validating client for postpaid update: {}", client.getId());
        if (client.getPlanType() != PlanType.POSTPAID) {
            logger.error("Invalid plan type for client ID: {}", client.getId());
            throw new InvalidPlanTypeException(PlanType.POSTPAID.getPlanName());
        }

        float newAmountLimit = postpaidDTO.getLimitValue();
        float spentAmount = client.getClientPostpaid().getSpentValue();

        if (newAmountLimit < spentAmount) {
            logger.error("New amount limit {} is less than the spent amount {} for client ID: {}", newAmountLimit, spentAmount, client.getId());
            throw new LimitValueExceededException(newAmountLimit, spentAmount);
        }

        if (newAmountLimit <= 0) {
            logger.error("Invalid limit value {} for client ID: {}", newAmountLimit, client.getId());
            throw new InvalidLimitValueException();
        }

        logger.debug("Client validation for postpaid update passed for client ID: {}", client.getId());
    }

    private void updateClientPostpaidData(Client client, PostpaidDTO postpaidDTO) {
        logger.debug("Updating postpaid data for client ID: {}", client.getId());
        client.getClientPostpaid().setLimitValue(postpaidDTO.getLimitValue());
    }
}
