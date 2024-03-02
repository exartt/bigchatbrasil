package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.MessageDTO;
import br.lms.bigchatbrasil.adapters.exceptions.BalanceException;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientMessage;
import br.lms.bigchatbrasil.domain.service.*;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientMessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ClientMessageService implements IClientMessageService {
    private final IClientService clientService;
    private final BigDecimal MESSAGE_FEE;
    private final ClientMessageRepository clientMessageRepository;
    private final ITwilioMessageSender twilioMessageSender;
    private final IClientPrepaidService clientPrepaidService;
    private final IClientPostpaidService clientPostpaidService;
    @Autowired
    public ClientMessageService(IClientService clientService, @Value("${message.fee}") String messageFee, ClientMessageRepository clientMessageRepository, ITwilioMessageSender twilioMessageSender, IClientPrepaidService clientPrepaidService, IClientPostpaidService clientPostpaidService) {
        this.clientService = clientService;
        this.MESSAGE_FEE = new BigDecimal(messageFee);
        this.clientMessageRepository = clientMessageRepository;
        this.twilioMessageSender = twilioMessageSender;
        this.clientPrepaidService = clientPrepaidService;
        this.clientPostpaidService = clientPostpaidService;
    }
    @Override
    public void sendMessage(MessageDTO messageDTO) {
        this.validateSufficientBalanceForMessage(messageDTO.getClientId());

        ClientMessage clientMessage = storeMessageAndRetrieveInfo(messageDTO);

        messageDTO.setClientTelephone(clientMessage.getClient().getTelephone());

        this.billMessage(clientMessage.getClient());

        String sid = twilioMessageSender.sendMessage(messageDTO);

        clientMessage.setSid(sid);

    }
    private void validateSufficientBalanceForMessage (long clientId) {
        BigDecimal balance = clientService.getAccountBalanceByClientId(clientId);
        if(!(balance.compareTo(MESSAGE_FEE) >= 0)) {
            throw new BalanceException();
        }
    }

    @Transactional
    private ClientMessage storeMessageAndRetrieveInfo (MessageDTO messageDTO) {
        Client client = clientService.getClientById(messageDTO.getClientId());

        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setClient(client);
        clientMessage.setTelephone(messageDTO.getTelephone());

        return clientMessageRepository.save(clientMessage);
    }

    private void billMessage (Client client) {
        switch (client.getPlanType()) {
            case PREPAID -> clientPrepaidService.deduceCredit(client);
            case POSTPAID -> clientPostpaidService.increaseSpentValue(client);
            default -> throw new IllegalArgumentException("Tipo de plano desconhecido");
        }
    }
}
