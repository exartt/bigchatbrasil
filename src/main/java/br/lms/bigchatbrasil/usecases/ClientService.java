package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.ClientDTO;
import br.lms.bigchatbrasil.adapters.dto.TypePlanDTO;
import br.lms.bigchatbrasil.adapters.mappers.ClientMapper;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPostpaid;
import br.lms.bigchatbrasil.domain.service.IClientService;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

@Service
public class ClientService implements IClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    @Transactional
    public Client getClientById (long clientId) {
        return clientRepository.getReferenceById(clientId);
    }

    @Async
    @Override
    public CompletableFuture<ClientDTO> getClientInformation (long clientId) {
        ClientDTO clientDTO = clientMapper.clientWithoutRelationsDTO(this.getClientById(clientId));
        return CompletableFuture.completedFuture(clientDTO);
    }
    @Async
    @Override
    public CompletableFuture<Void> getClientBalance (long clientId) {
        this.getAccountBalanceByClientId(clientId);
        return CompletableFuture.completedFuture(null);
    }
    @Override
    public BigDecimal getAccountBalanceByClientId (long clientId) {
         Client client = this.getClientById(clientId);
        if(client.getPlanType() == PlanType.PREPAID) {
           return BigDecimal.valueOf(client.getClientPrepaid().getAmountCredit());
        }
        return this.getPostpaidBalance(client.getClientPostpaid());
    }

    @Async
    @Override
    @Transactional
    public CompletableFuture<Void> updateTypePlan(long clientId, TypePlanDTO typePlanDTO) {
        Client client = this.getClientById(clientId);
        client.setPlanType(typePlanDTO.getPlanType());
        return CompletableFuture.completedFuture(null);
    }

    private BigDecimal getPostpaidBalance(ClientPostpaid clientPostpaid) {
        BigDecimal limitValue = BigDecimal.valueOf(clientPostpaid.getLimitValue());
        BigDecimal spentValue = BigDecimal.valueOf(clientPostpaid.getSpentValue());

        return limitValue.subtract(spentValue).setScale(2, RoundingMode.HALF_EVEN);
    }

}
