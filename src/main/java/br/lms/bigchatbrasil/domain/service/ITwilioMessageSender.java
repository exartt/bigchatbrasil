package br.lms.bigchatbrasil.domain.service;

import br.lms.bigchatbrasil.adapters.dto.MessageDTO;

public interface ITwilioMessageSender {
    String sendMessage (MessageDTO messageDTO);
}
