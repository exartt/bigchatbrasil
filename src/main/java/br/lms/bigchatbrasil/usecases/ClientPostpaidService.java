package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidLimitValueException;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidPlanTypeException;
import br.lms.bigchatbrasil.adapters.exceptions.LimitValueExceededException;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPostpaid;
import br.lms.bigchatbrasil.domain.service.IClientPostpaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientPostpaidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ClientPostpaidService implements IClientPostpaidService {
    private final ClientPostpaidRepository clientPostpaidRepository;
    private final IClientService clientService;
    private final BigDecimal MESSAGE_FEE;
    @Autowired
    public ClientPostpaidService(ClientPostpaidRepository clientPostpaidRepository,
                                 IClientService clientService,
                                 @Value("${message.fee}") String messageFee) {
        this.clientPostpaidRepository = clientPostpaidRepository;
        this.clientService = clientService;
        this.MESSAGE_FEE = new BigDecimal(messageFee);
    }

    @Override
    @Transactional
    public void updateLimitAmount(long clientId, PostpaidDTO postpaidDTO) {
        Client client = clientService.getClientById(clientId);
        this.validateClientForPostpaidUpdate(client, postpaidDTO);

        ClientPostpaid clientPostpaid = this.updateClientPostpaidData(client, postpaidDTO);
        client.setClientPostpaid(clientPostpaid);
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
        float spentAmount = client.getClientPostpaid().getLimitValue();

        if (newAmountLimit > spentAmount) {
            throw new LimitValueExceededException(newAmountLimit, spentAmount);
        }

        if (newAmountLimit <= 0) {
            throw new InvalidLimitValueException();
        }
    }

    private ClientPostpaid updateClientPostpaidData(Client client, PostpaidDTO postpaidDTO) {
        ClientPostpaid clientPostpaid = new ClientPostpaid();
        clientPostpaid.setId(client.getId());
        clientPostpaid.setSpentValue(client.getClientPostpaid().getSpentValue());
        clientPostpaid.setLimitValue(postpaidDTO.getLimitValue());
        return clientPostpaid;
    }
}
