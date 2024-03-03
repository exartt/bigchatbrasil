package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.adapters.dto.MessageDTO;
import br.lms.bigchatbrasil.domain.enums.PlanType;
import br.lms.bigchatbrasil.domain.model.Client;
import br.lms.bigchatbrasil.domain.model.ClientMessage;
import br.lms.bigchatbrasil.domain.model.ClientPostpaid;
import br.lms.bigchatbrasil.domain.service.IClientPostpaidService;
import br.lms.bigchatbrasil.domain.service.IClientPrepaidService;
import br.lms.bigchatbrasil.domain.service.IClientService;
import br.lms.bigchatbrasil.domain.service.ITwilioMessageSender;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ClientMessageServiceTest {
    @Mock
    private IClientService clientService;

    @Mock
    private ClientMessageRepository clientMessageRepository;

    @Mock
    private ITwilioMessageSender twilioMessageSender;

    @Mock
    private IClientPrepaidService clientPrepaidService;

    @Mock
    private IClientPostpaidService clientPostpaidService;

    private ClientMessageService clientMessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        BigDecimal messageFee = new BigDecimal("0.25");

        clientMessageService = new ClientMessageService(
                clientService,
                messageFee.toString(),
                clientMessageRepository,
                twilioMessageSender,
                clientPrepaidService,
                clientPostpaidService
        );
    }

    @Test
    public void testSendMessage() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setClientId(1L);

        BigDecimal returnedBalance = new BigDecimal("10.00");

        Client mockClient = this.createMockClient();
        ClientMessage mockClientMessage = this.mockClientMessage(mockClient);

        when(clientService.getClientReferenceById(anyLong())).thenReturn(mockClient);
        when(clientService.getAccountBalanceByClientId(messageDTO.getClientId())).thenReturn(returnedBalance);
        when(clientMessageRepository.save(any())).thenReturn(mockClientMessage);

        doNothing().when(clientPrepaidService).deduceCredit(any(Client.class));
        doNothing().when(clientPostpaidService).increaseSpentValue(any(Client.class));

        clientMessageService.sendMessage(messageDTO);

        verify(clientService).getClientReferenceById(anyLong());
        verify(clientMessageRepository).save(any(ClientMessage.class));
        verify(twilioMessageSender).sendMessage(messageDTO);
    }

    private ClientMessage mockClientMessage (Client client) {
        ClientMessage clientMessage = new ClientMessage();
        clientMessage.setSid("0000000");
        clientMessage.setMessage("Mensagem teste");
        clientMessage.setWhatsapp(true);
        clientMessage.setClient(client);
        clientMessage.setTelephone("+5543999999999");
        clientMessage.setVersion(0);
        return clientMessage;
    }
    private Client createMockClient() {
        Client client = new Client();
        client.setId(1L);
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