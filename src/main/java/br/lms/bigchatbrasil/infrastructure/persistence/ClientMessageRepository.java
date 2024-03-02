package br.lms.bigchatbrasil.infrastructure.persistence;

import br.lms.bigchatbrasil.domain.model.ClientMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientMessageRepository extends JpaRepository<ClientMessage, Long> {
}
