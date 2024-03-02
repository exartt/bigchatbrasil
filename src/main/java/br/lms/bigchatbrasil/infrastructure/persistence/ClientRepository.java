package br.lms.bigchatbrasil.infrastructure.persistence;

import br.lms.bigchatbrasil.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
