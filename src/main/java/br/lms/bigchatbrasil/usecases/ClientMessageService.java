package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.MessageDTO;
import br.lms.bigchatbrasil.adapters.exceptions.InsufficientBalanceException;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientMessage;
import br.lms.bigchatbrasil.domain.service.*;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientMessageRepository;
import com.twilio.exception.TwilioException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ClientMessageService implements IClientMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ClientMessageService.class);
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
        logger.info("Starting to send message for client ID: {}", messageDTO.getClientId());

        this.validateSufficientBalanceForMessage(messageDTO.getClientId());

        ClientMessage clientMessage = storeMessageAndRetrieveInfo(messageDTO);

        messageDTO.setClientTelephone(clientMessage.getClient().getTelephone());

        this.billMessage(clientMessage.getClient());

        /*
        * caso não queira criar credenciais no twilio o envio será ignorado.
        * Caso queria siga a documentação de criação do twilio:
        * https://www.twilio.com/docs/messaging/quickstart/java
        * e adicione no .env as credenciais para uso.
        * */
        try {
            String sid = twilioMessageSender.sendMessage(messageDTO);
            clientMessage.setSid(sid);
        } catch (Exception e) {
            logger.error("Error sending message with Twilio: " + e.getMessage(), e);
        }
    }
    private void validateSufficientBalanceForMessage (long clientId) {
        logger.debug("Validating balance for client ID: {}", clientId);
        BigDecimal balance = clientService.getAccountBalanceByClientId(clientId);
        if(!(balance.compareTo(MESSAGE_FEE) >= 0)) {
            logger.warn("Insufficient balance for client ID: {}", clientId);
            throw new InsufficientBalanceException();
        }
        logger.debug("Sufficient balance confirmed for client ID: {}", clientId);
    }

    @Transactional(dontRollbackOn = {TwilioException.class})
    private ClientMessage storeMessageAndRetrieveInfo (MessageDTO messageDTO) {
        Client client = clientService.getClientReferenceById(messageDTO.getClientId());

        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setClient(client);
        clientMessage.setTelephone(messageDTO.getRawTelephone());
        clientMessage.setWhatsapp(client.isWhatsapp());
        clientMessage.setMessage(messageDTO.getMessage());
        clientMessage.setSid("0000000000");
        clientMessage.setVersion(0);

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
