package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.ClientDTO;
import br.lms.bigchatbrasil.adapters.dto.TypePlanDTO;
import br.lms.bigchatbrasil.adapters.mappers.ClientMapper;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPostpaid;
import br.lms.bigchatbrasil.domain.service.IClientService;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

@Service
public class ClientService implements IClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    @Transactional
    public Client getClientReferenceById(long clientId) {
        logger.debug("Retrieving client reference for client ID: {}", clientId);
        return clientRepository.getReferenceById(clientId);
    }
    @Async
    @Override
    public CompletableFuture<ClientDTO> getClientInformation (long clientId) {
        logger.info("Getting client information for client ID: {}", clientId);
        return CompletableFuture.completedFuture(
                clientMapper.clientWithoutRelationsDTO(
                        this.getClientReferenceById(clientId)));
    }
    @Override
    public CompletableFuture<BigDecimal> getClientBalance (long clientId) {
        logger.info("Retrieving balance for client ID: {}", clientId);
        return CompletableFuture.completedFuture(this.getAccountBalanceByClientId(clientId));
    }
    @Override
    public BigDecimal getAccountBalanceByClientId (long clientId) {
        Client client = this.getClientReferenceById(clientId);
        logger.debug("Determining account balance for client ID: {}, Plan Type: {}", clientId, client.getPlanType());
        if(client.getPlanType() == PlanType.PREPAID) {
           return BigDecimal.valueOf(client.getClientPrepaid().getAmountCredit());
        }
        return this.getPostpaidBalance(client.getClientPostpaid());
    }
    @Async
    @Override
    @Transactional
    public CompletableFuture<Void> updateTypePlan(long clientId, TypePlanDTO typePlanDTO) {
        logger.info("Updating plan type for client ID: {} to {}", clientId, typePlanDTO.getPlanType());
        Client client = this.getClientReferenceById(clientId);
        client.setPlanType(typePlanDTO.getPlanType());
        return CompletableFuture.completedFuture(null);
    }

    private BigDecimal getPostpaidBalance(ClientPostpaid clientPostpaid) {
        logger.debug("Calculating postpaid balance for clientPostpaid ID: {}", clientPostpaid.getId());
        BigDecimal limitValue = BigDecimal.valueOf(clientPostpaid.getLimitValue());
        BigDecimal spentValue = BigDecimal.valueOf(clientPostpaid.getSpentValue());

        return limitValue.subtract(spentValue).setScale(2, RoundingMode.HALF_EVEN);
    }

}
