package br.lms.bigchatbrasil.infrastructure.twillio;

import br.lms.bigchatbrasil.adapters.dto.MessageDTO;
import br.lms.bigchatbrasil.domain.service.ITwilioMessageSender;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwilioMessageSender implements ITwilioMessageSender {

    @Value("${TWILIO_ACCOUNT_SID}")
    private String accountSid;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String authToken;

    @PostConstruct
    public void initializeTwilio() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public String sendMessage(MessageDTO messageDTO) {
        Message message = Message.creator(
                new PhoneNumber(messageDTO.getTelephone()),
                new PhoneNumber(messageDTO.getClientTelephone()),
                messageDTO.getMessage()
        ).create();

        return message.getSid();
    }
}
