package org.angel.test.springboot.app.repositories;

import org.angel.test.springboot.app.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // List<Account> findAll();
    // Account findById(Long id);
    // void update(Account account);
}
