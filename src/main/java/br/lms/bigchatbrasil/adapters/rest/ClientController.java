package br.lms.bigchatbrasil.adapters.rest;

import br.lms.bigchatbrasil.adapters.dto.ClientDTO;
import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.adapters.dto.TypePlanDTO;
import br.lms.bigchatbrasil.domain.service.IClientPostpaidService;
import br.lms.bigchatbrasil.domain.service.IClientPrepaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Transactional
@RequestMapping(value = "/client")
public class ClientController {

    private final IClientService clientService;
    private final IClientPostpaidService clientPostpaidService;
    private final IClientPrepaidService clientPrepaidService;
    private final SimpMessagingTemplate template;

    public ClientController(IClientService clientService,
                            IClientPostpaidService clientPostpaidService,
                            IClientPrepaidService clientPrepaidService,
                            SimpMessagingTemplate template) {
        this.clientService = clientService;
        this.clientPostpaidService = clientPostpaidService;
        this.clientPrepaidService = clientPrepaidService;
        this.template = template;
    }

    @GetMapping("/get-information/{clientId}")
    public ClientDTO getClientById(@PathVariable long clientId) {
        return clientService.getClientInformation(clientId);
    }

    @GetMapping("/get-balance/{clientId}")
    public BigDecimal getAccountBalanceByClientId(@PathVariable long clientId) {
        return clientService.getClientBalance(clientId);
    }

    @PostMapping("/insert-credit")
    public ResponseEntity<HttpStatus> insertCredit (@RequestBody CreditDTO creditDTO) {
        clientPrepaidService.insertCredit(creditDTO).thenAccept(unused ->
                template.convertAndSend("/topic/status", "Operação bem sucedida"));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/change-limit/{clientId}")
    public ResponseEntity<HttpStatus> setNewLimitAmount (@PathVariable long clientId, @RequestBody PostpaidDTO postpaidDTO) {
        clientPostpaidService.updateLimitAmount(clientId, postpaidDTO).thenAccept(unused ->
                template.convertAndSend("/topic/status", "Operação bem sucedida, novo saldo limite definido."));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/change-plan/{clientId}")
    public ResponseEntity<HttpStatus> changeTypePlan (@PathVariable long clientId, @RequestBody TypePlanDTO typePlanDTO) {
        clientService.updateTypePlan(clientId, typePlanDTO).thenAccept(unused ->
                template.convertAndSend("/topic/status", "Operação bem sucedida, novo plano de selecionado: " + typePlanDTO.getPlanType().getPlanName()));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
