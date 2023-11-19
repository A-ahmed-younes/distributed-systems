package ma.enset.accountcqrses.query.repositories;

import ma.enset.accountcqrses.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
