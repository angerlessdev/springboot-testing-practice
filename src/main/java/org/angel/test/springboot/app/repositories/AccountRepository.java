package org.angel.test.springboot.app.repositories;

import org.angel.test.springboot.app.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // @Query("select c from Account c where c.person=?1")
    Optional<Account> findByPerson(String person);
}
