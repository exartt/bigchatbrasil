package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidLimitValueException;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidPlanTypeException;
import br.lms.bigchatbrasil.adapters.exceptions.LimitValueExceededException;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPostpaid;
import br.lms.bigchatbrasil.domain.service.IClientPrepaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements IClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private IClientPrepaidService clientPrepaidService;

    public Client getClientById (long clientId) {
        return clientRepository.getReferenceById(clientId);
    }

    /*
    * Incluir creditos para um cliente
    * Precisamos verificar se o qual o plano do cliente
    * baseado nisso chamar a função do service do prepaid a função de insertCreditByClientId
    * se for possível, no final implementar algum tipo de websocket para enviar a resposta (tendo em vista).
    */
    public void insertCredit (CreditDTO creditDTO) {
        Client client = this.getClientById(creditDTO.getClientId());
        if(client.getPlanType() == PlanType.PREPAID) {
            clientPrepaidService.insertCreditByClient(client, creditDTO.getAmountCredit());
        } else {
            // throw exception
        }
    }

    @Override
    public void updateLimitAmount(long clientId, PostpaidDTO postpaidDTO) {
        Client client = getClientById(clientId);
        validateClientForPostpaidUpdate(client, postpaidDTO);

        ClientPostpaid clientPostpaid = updateClientPostpaidData(client, postpaidDTO);
        client.setClientPostpaid(clientPostpaid);
    }


    public void getAccountBalanceByClientId (long clientId) {
        Client client = this.getClientById(clientId);
        if(client.getPlanType() == PlanType.PREPAID) {
            client.getClientPrepaid();
        } else {
            client.getClientPostpaid();
        }
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
