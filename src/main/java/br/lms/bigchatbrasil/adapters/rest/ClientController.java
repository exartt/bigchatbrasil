package br.lms.bigchatbrasil.adapters.rest;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.adapters.dto.TypePlanDTO;
import br.lms.bigchatbrasil.domain.service.IClientPostpaidService;
import br.lms.bigchatbrasil.domain.service.IClientPrepaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequestMapping(value = "/client")
public class ClientController {

    private final IClientService clientService;
    private final IClientPostpaidService clientPostpaidService;
    private final IClientPrepaidService clientPrepaidService;

    public ClientController(IClientService clientService, IClientPostpaidService clientPostpaidService, IClientPrepaidService clientPrepaidService) {
        this.clientService = clientService;
        this.clientPostpaidService = clientPostpaidService;
        this.clientPrepaidService = clientPrepaidService;
    }

    @GetMapping("/get-information/{clientId}")
    public ResponseEntity<HttpStatus> getClientById(@PathVariable long clientId) {
        clientService.getClientById(clientId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-balance/{clientId}")
    public ResponseEntity<HttpStatus> getAccountBalanceByClientId(@PathVariable long clientId) {
        clientService.getAccountBalanceByClientId(clientId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PostMapping("/insert-credit")
    public ResponseEntity<HttpStatus> insertCredit (@RequestBody CreditDTO creditDTO) {
        clientPrepaidService.insertCredit(creditDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/change-limit/{clientId}")
    public ResponseEntity<HttpStatus> setNewLimitAmount (@PathVariable long clientId, @RequestBody PostpaidDTO postpaidDTO) {
        clientPostpaidService.updateLimitAmount(clientId, postpaidDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/change-plan/{clientId}")
    public ResponseEntity<HttpStatus> changeTypePlan (@PathVariable long clientId, @RequestBody TypePlanDTO typePlanDTO) {
        clientService.updateTypePlan(clientId, typePlanDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
