package org.angel.test.springboot.app.repositories;

import org.angel.test.springboot.app.models.Bank;

import java.util.List;

public interface BankRepository {
    List<Bank> findAll();
    Bank findById(Long id);
    void update(Bank bank);
}
