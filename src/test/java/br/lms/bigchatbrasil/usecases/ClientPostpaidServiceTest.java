package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.PostpaidDTO;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPostpaid;
import br.lms.bigchatbrasil.domain.service.IClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientPostpaidServiceTest {
    @Mock
    private IClientService clientService;
    @InjectMocks
    private ClientPostpaidService clientPostpaidService;
    @BeforeEach
    void setUp() {
        clientService = mock(IClientService.class);
        clientPostpaidService = new ClientPostpaidService(clientService, "0.25");
    }

    @Test
    public void testUpdateLimitAmount() {
        long clientId = 1L;

        PostpaidDTO postpaidDTO = new PostpaidDTO();
        postpaidDTO.setLimitValue(1000F);
        postpaidDTO.setSpentValue(0F);

        Client mockClient = this.createMockClient();

        when(clientService.getClientReferenceById(clientId)).thenReturn(mockClient);

        CompletableFuture<Void> future = clientPostpaidService.updateLimitAmount(clientId, postpaidDTO);
        future.join();

        verify(clientService).getClientReferenceById(clientId);

        assertEquals(1000F, mockClient.getClientPostpaid().getLimitValue());
    }

    @Test
    public void testIncreaseSpentValue() {
        ClientPostpaid clientPostpaidMock = mock(ClientPostpaid.class);

        Client client = this.createMockClient();
        client.setClientPostpaid(clientPostpaidMock);

        when(clientPostpaidMock.getSpentValue()).thenReturn(100F);

        clientPostpaidService.increaseSpentValue(client);

        verify(clientPostpaidMock).setSpentValue(100F + 0.25F);
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
        client.setPlanType(PlanType.POSTPAID);
        client.setWhatsapp(true);

        ClientPostpaid clientPostpaid = new ClientPostpaid();
        clientPostpaid.setId(1L);
        clientPostpaid.setClient(client);
        clientPostpaid.setLimitValue(50F);
        clientPostpaid.setSpentValue(0F);

        client.setClientPostpaid(clientPostpaid);

        return client;
    }
}