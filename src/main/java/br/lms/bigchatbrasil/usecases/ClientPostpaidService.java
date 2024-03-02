package br.lms.bigchatbrasil.usecases;

import br.lms.bigchatbrasil.domain.service.IClientPostpaidService;
import br.lms.bigchatbrasil.domain.service.IClientPrepaidService;
import br.lms.bigchatbrasil.infrastructure.persistence.ClientPostpaidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientPostpaidService implements IClientPostpaidService {
    @Autowired
    private ClientPostpaidRepository clientPostpaidRepository;
}
