package br.lms.bigchatbrasil.adapters.mappers;

import br.lms.bigchatbrasil.adapters.dto.ClientDTO;
import br.lms.bigchatbrasil.domain.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public ClientDTO clientWithoutRelationsDTO (Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setCpf(client.getCpf());
        dto.setCnpj(client.getCnpj());
        dto.setCompanyName(client.getCompanyName());
        dto.setPlanType(client.getPlanType().name());

        return dto;
    }
}
