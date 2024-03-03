package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.CreditDTO;
import br.lms.bigchatbrasil.adapters.exceptions.InvalidPlanTypeException;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPrepaid;
import br.lms.bigchatbrasil.domain.service.IClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ClientPrepaidServiceTest {
    private IClientService clientService;

    private ClientPrepaidService clientPrepaidService;
    @BeforeEach
    public void setup() {
        clientService = mock(IClientService.class);
        clientPrepaidService = new ClientPrepaidService(clientService, "0.25");
    }
    @Test
    public void testInsertCreditWithNonPrepaidPlan() {
        CreditDTO creditDTO = this.getClientDTO();

        Client client = this.createMockNonPrepaidClient();

        when(clientService.getClientReferenceById(creditDTO.getClientId())).thenReturn(client);

        assertThrows(InvalidPlanTypeException.class, () -> {
            CompletableFuture<Float> future = clientPrepaidService.insertCredit(creditDTO);
            future.get();
        });
    }

    @Test
    public void testDeduceCredit() {
        Client client = createMockPrepaidClient();
        float initialCredit = client.getClientPrepaid().getAmountCredit();
        BigDecimal expectedNewCredit = BigDecimal.valueOf(initialCredit).subtract(new BigDecimal("0.25"));

        clientPrepaidService.deduceCredit(client);

        assertEquals(expectedNewCredit.floatValue(), client.getClientPrepaid().getAmountCredit());
    }

    @Test
    public void testInsertCreditPrepaidPlan() {
        CreditDTO creditDTO = getClientDTO();
        Client mockClient = createMockPrepaidClient();

        when(clientService.getClientReferenceById(creditDTO.getClientId())).thenReturn(mockClient);

        CompletableFuture<Float> future = clientPrepaidService.insertCredit(creditDTO);
        future.join();

        float updatedCredit = mockClient.getClientPrepaid().getAmountCredit() + creditDTO.getAmountCredit();
        mockClient.getClientPrepaid().setAmountCredit(updatedCredit);

        assertEquals(updatedCredit, mockClient.getClientPrepaid().getAmountCredit());
    }

    private Client createMockPrepaidClient() {
        Client client = this.createMockClient();

        ClientPrepaid clientPrepaid = new ClientPrepaid();

        clientPrepaid.setId(1L);
        clientPrepaid.setClient(client);
        clientPrepaid.setAmountCredit(500F);

        client.setClientPrepaid(clientPrepaid);

        return client;
    }

    private Client createMockNonPrepaidClient() {
        Client client = this.createMockClient();

        client.setPlanType(PlanType.POSTPAID);

        return client;
    }


    private Client createMockClient() {
        Client client = new Client();
        client.setId(1L); // ID do cliente
        client.setName("João Silva");
        client.setEmail("joao.silva@example.com");
        client.setTelephone("11987654321");
        client.setCpf("123.456.789-00");
        client.setCnpj("12.345.678/0001-00");
        client.setCompanyName("Empresa do João");
        client.setPlanType(PlanType.PREPAID);
        client.setWhatsapp(true);

        return client;
    }

    private CreditDTO getClientDTO () {
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setClientId(1);
        creditDTO.setAmountCredit(100F);
        return creditDTO;
    }
}