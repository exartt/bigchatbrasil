package br.lms.bigchatbrasil.infrastructure.persistence;

import br.lms.bigchatbrasil.domain.model.ClientPrepaid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientPrepaidRepository extends JpaRepository<ClientPrepaid, Long> {
}
