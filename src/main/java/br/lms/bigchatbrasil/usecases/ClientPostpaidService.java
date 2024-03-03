package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidLimitValueException;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidPlanTypeException;
import br.lms.bigchatbrasil.adapters.exceptions.LimitValueExceededException;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.service.IClientPostpaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class ClientPostpaidService implements IClientPostpaidService {
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
        Client client = clientService.getClientReferenceById(clientId);
        validateClientForPostpaidUpdate(client, postpaidDTO);
        updateClientPostpaidData(client, postpaidDTO);
        return CompletableFuture.completedFuture(null);
    }
    @Override
    public void increaseSpentValue(Client client) {
        BigDecimal oldBalance = BigDecimal.valueOf(client.getClientPostpaid().getSpentValue());
        BigDecimal newBalance = oldBalance.add(MESSAGE_FEE);
        client.getClientPostpaid().setSpentValue(newBalance.floatValue());
    }

    private void validateClientForPostpaidUpdate(Client client, PostpaidDTO postpaidDTO) {
        if (client.getPlanType() != PlanType.POSTPAID) {
            throw new InvalidPlanTypeException(PlanType.POSTPAID.getPlanName());
        }

        float newAmountLimit = postpaidDTO.getLimitValue();
        float spentAmount = client.getClientPostpaid().getSpentValue();

        if (newAmountLimit < spentAmount) {
            throw new LimitValueExceededException(newAmountLimit, spentAmount);
        }

        if (newAmountLimit <= 0) {
            throw new InvalidLimitValueException();
        }
    }

    private void updateClientPostpaidData(Client client, PostpaidDTO postpaidDTO) {
        client.getClientPostpaid().setLimitValue(postpaidDTO.getLimitValue());
    }
}
