package org.angel.test.springboot.app.services;

import org.angel.test.springboot.app.models.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<Account> findAll();
    Account save(Account account);
    Account findAccountById(Long id);
    int checkTotalTransfers(Long bankId);
    BigDecimal checkBalance(Long accountId);
    void transfer(Long sourceAccountNumber, Long destinationAccountNumber, BigDecimal amount, Long bankId);
}
