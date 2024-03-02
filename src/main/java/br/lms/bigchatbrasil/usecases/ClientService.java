package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.TypePlanDTO;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPostpaid;
import br.lms.bigchatbrasil.domain.service.IClientService;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ClientService implements IClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // necessário colocar o transactional na operação, mesmo ela sendo de leitura, pois estou utilizando ela para updates.
    @Override
    @Transactional
    public Client getClientById (long clientId) {
        return clientRepository.getReferenceById(clientId);
    }

    @Override
    public BigDecimal getAccountBalanceByClientId (long clientId) {
        Client client = this.getClientById(clientId);
        if(client.getPlanType() == PlanType.PREPAID) {
           return BigDecimal.valueOf(client.getClientPrepaid().getAmountCredit());
        }
        return this.getPostpaidBalance(client.getClientPostpaid());
    }

    @Override
    @Transactional
    public void updateTypePlan(long clientId, TypePlanDTO typePlanDTO) {
        Client client = this.getClientById(clientId);
        client.setPlanType(typePlanDTO.getPlanType());
    }

    private BigDecimal getPostpaidBalance(ClientPostpaid clientPostpaid) {
        BigDecimal limitValue = BigDecimal.valueOf(clientPostpaid.getLimitValue());
        BigDecimal spentValue = BigDecimal.valueOf(clientPostpaid.getSpentValue());

        return limitValue.subtract(spentValue).setScale(2, RoundingMode.HALF_EVEN);
    }

}
