package br.lms.bigchatbrasil.adapters.rest;

import br.lms.bigchatbrasil.adapters.dto.MessageDTO;
import br.lms.bigchatbrasil.domain.service.IClientMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequestMapping(value = "/message")
public class MessageController {
    private final IClientMessageService clientMessageService;
    public MessageController(IClientMessageService clientMessageService) {
        this.clientMessageService = clientMessageService;
    }

    @PostMapping("/send")
    public ResponseEntity<HttpStatus> sendMessage (@RequestBody MessageDTO messageDTO) {
        clientMessageService.sendMessage(messageDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
