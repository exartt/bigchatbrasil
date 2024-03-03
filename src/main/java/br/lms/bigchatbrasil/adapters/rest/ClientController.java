package br.lms.bigchatbrasil.adapters.rest;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.adapters.dto.TypePlanDTO;
import br.lms.bigchatbrasil.adapters.mappers.ClientMapper;
import br.lms.bigchatbrasil.domain.service.IClientPostpaidService;
import br.lms.bigchatbrasil.domain.service.IClientPrepaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<HttpStatus> getClientById(@PathVariable long clientId) {
        clientService.getClientInformation(clientId).thenAccept(clientDTO ->
                template.convertAndSend("/topic/client-information", clientDTO));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-balance/{clientId}")
    public ResponseEntity<HttpStatus> getAccountBalanceByClientId(@PathVariable long clientId) {
        clientService.getClientBalance(clientId).thenAccept(unused ->
                template.convertAndSend("/topic/client-balance", "Operação bem sucedida, novo plano de selecionado: "));
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
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
