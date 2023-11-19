package ma.enset.accountcqrses.query.repositories;

import ma.enset.accountcqrses.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, String> {
}
