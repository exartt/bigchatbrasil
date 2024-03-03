package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.ClientDTO;
import br.lms.bigchatbrasil.adapters.dto.TypePlanDTO;
import br.lms.bigchatbrasil.adapters.mappers.ClientMapper;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientPostpaid;
import br.lms.bigchatbrasil.domain.model.ClientPrepaid;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetClientReferenceById() {
        long clientId = 1L;
        Client expectedClient = new Client();
        when(clientRepository.getReferenceById(clientId)).thenReturn(expectedClient);

        Client result = clientService.getClientReferenceById(clientId);

        assertEquals(expectedClient, result);
        verify(clientRepository).getReferenceById(clientId);
    }

    @Test
    public void testGetClientInformation() throws Exception {
        long clientId = 1L;
        ClientDTO expectedDto = new ClientDTO(); // Substitua com a classe real
        Client client = new Client();

        when(clientRepository.getReferenceById(clientId)).thenReturn(client);
        when(clientMapper.clientWithoutRelationsDTO(client)).thenReturn(expectedDto);

        CompletableFuture<ClientDTO> future = clientService.getClientInformation(clientId);
        ClientDTO result = future.join();

        assertEquals(expectedDto, result);
        verify(clientRepository).getReferenceById(clientId);
        verify(clientMapper).clientWithoutRelationsDTO(client);
    }
    @Test
    public void testGetAccountBalanceByClientIdPrepaid() {
        long clientId = 1L;
        Client client = new Client();
        client.setPlanType(PlanType.PREPAID);
        ClientPrepaid clientPrepaid = new ClientPrepaid();
        clientPrepaid.setAmountCredit(100.0F);
        client.setClientPrepaid(clientPrepaid);

        when(clientRepository.getReferenceById(clientId)).thenReturn(client);

        BigDecimal result = clientService.getAccountBalanceByClientId(clientId);

        assertEquals(BigDecimal.valueOf(100.0), result);
        verify(clientRepository).getReferenceById(clientId);
    }

    @Test
    public void testGetAccountBalanceByClientIdPostpaid() {
        long clientId = 1L;
        Client client = new Client();
        client.setPlanType(PlanType.POSTPAID);
        ClientPostpaid clientPostpaid = new ClientPostpaid();
        clientPostpaid.setLimitValue(200.0F);
        clientPostpaid.setSpentValue(50.0F);
        client.setClientPostpaid(clientPostpaid);

        when(clientRepository.getReferenceById(clientId)).thenReturn(client);

        BigDecimal result = clientService.getAccountBalanceByClientId(clientId);

        assertEquals(BigDecimal.valueOf(150.0).setScale(2, RoundingMode.HALF_EVEN), result);
        verify(clientRepository).getReferenceById(clientId);
    }
    // Preciso ajustar.
//    @Test
//    public void testUpdateTypePlan() {
//        long clientId = 1L;
//        PlanType planType = PlanType.PREPAID;
//        TypePlanDTO typePlanDTO = new TypePlanDTO();
//        typePlanDTO.setTypePlan(planType.toString());
//        Client client = this.createMockClient();
//
//        when(clientRepository.getReferenceById(clientId)).thenReturn(client);
//        when(clientRepository.save(any(Client.class))).thenReturn(client);
//
//        CompletableFuture<Void> future = clientService.updateTypePlan(clientId, typePlanDTO);
//        future.join();
//
//        assertEquals(planType, client.getPlanType());
//        verify(clientRepository).getReferenceById(clientId);
//        verify(clientRepository).save(any(Client.class));
//    }

    private Client createMockClient() {
        Client client = new Client();
        client.setId(1L);
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
}