package org.angel.test.springboot.app.repositories;

import org.angel.test.springboot.app.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    // List<Bank> findAll();
    // Bank findById(Long id);
    // void update(Bank bank);
}
