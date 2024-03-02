package br.lms.bigchatbrasil.adapters.rest;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.domain.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private final IClientService clientService;

    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<HttpStatus> getClientById(@PathVariable long clientId) {
        clientService.getClientById(clientId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> insertCredit (@RequestBody CreditDTO creditDTO) {
        clientService.insertCredit(creditDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{clientId}")
    public ResponseEntity<HttpStatus> insertCredit (@PathVariable long clientId, @RequestBody PostpaidDTO postpaidDTO) {
        clientService.updateLimitAmount(clientId, postpaidDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
