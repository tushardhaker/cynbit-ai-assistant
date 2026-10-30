package cynbit_assistant.repository;

import cynbit_assistant.entity.ClientLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientLeadRepository extends JpaRepository<ClientLead, Long> {
}