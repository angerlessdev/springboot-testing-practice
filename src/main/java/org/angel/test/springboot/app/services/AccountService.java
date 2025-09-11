package org.angel.test.springboot.app.services;

import org.angel.test.springboot.app.models.Account;

import java.math.BigDecimal;

public interface AccountService {
    Account findAccountById(Long id);
    int checkTotalTransfers(Long bankId);
    BigDecimal checkBalance(Long accountId);
    void transfer(Long sourceAccountNumber, Long destinationAccountNumber, BigDecimal amount);
}
