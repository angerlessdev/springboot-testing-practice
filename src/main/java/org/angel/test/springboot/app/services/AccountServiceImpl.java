package org.angel.test.springboot.app.services;

import org.angel.test.springboot.app.models.Account;
import org.angel.test.springboot.app.models.Bank;
import org.angel.test.springboot.app.repositories.AccountRepository;
import org.angel.test.springboot.app.repositories.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Account findAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int checkTotalTransfers(Long bankId) {
        return bankRepository.findById(bankId).orElseThrow().getTotalTransfers();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal checkBalance(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow().getBalance();
    }

    @Override
    @Transactional
    public void transfer(Long sourceAccountNumber, Long destinationAccountNumber, BigDecimal amount, Long bankId) {
        Account sourceAccount = accountRepository.findById(sourceAccountNumber).orElseThrow();
        sourceAccount.debit(amount);
        accountRepository.save(sourceAccount);

        Account destinationAccount = accountRepository.findById(destinationAccountNumber).orElseThrow();
        destinationAccount.credit(amount);
        accountRepository.save(destinationAccount);

        Bank bank = bankRepository.findById(bankId).orElseThrow();
        int totalTransfers = bank.getTotalTransfers();
        bank.setTotalTransfers(++totalTransfers);
        bankRepository.save(bank);
    }
}
