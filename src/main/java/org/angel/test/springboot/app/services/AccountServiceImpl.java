package org.angel.test.springboot.app.services;

import org.angel.test.springboot.app.models.Account;
import org.angel.test.springboot.app.models.Bank;
import org.angel.test.springboot.app.repositories.AccountRepository;
import org.angel.test.springboot.app.repositories.BankRepository;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public int checkTotalTransfers(Long bankId) {
        return bankRepository.findById(bankId).getTotalTransfers();
    }

    @Override
    public BigDecimal checkBalance(Long accountId) {
        return accountRepository.findById(accountId).getBalance();
    }

    @Override
    public void transfer(Long sourceAccountNumber, Long destinationAccountNumber, BigDecimal amount) {
        Bank bank = bankRepository.findById(1L);
        int totalTransfers = bank.getTotalTransfers();
        bank.setTotalTransfers(++totalTransfers);
        bankRepository.update(bank);

        Account sourceAccount = accountRepository.findById(sourceAccountNumber);
        sourceAccount.debit(amount);
        accountRepository.update(sourceAccount);

        Account destinationAccount = accountRepository.findById(destinationAccountNumber);
        destinationAccount.credit(amount);
        accountRepository.update(destinationAccount);
    }
}
