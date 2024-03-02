package br.lms.bigchatbrasil.infrastructure.persistence;

import br.lms.bigchatbrasil.domain.model.ClientPostpaid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientPostpaidRepository extends JpaRepository<ClientPostpaid, Long> {
}
