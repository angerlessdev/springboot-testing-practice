package org.angel.test.springboot.app;

import org.angel.test.springboot.app.exceptions.InsufficientFundsException;
import org.angel.test.springboot.app.models.Account;
import org.angel.test.springboot.app.models.Bank;
import org.angel.test.springboot.app.repositories.AccountRepository;
import org.angel.test.springboot.app.repositories.BankRepository;
import org.angel.test.springboot.app.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {
    @MockitoBean
    AccountRepository accountRepository;
    @MockitoBean
    BankRepository bankRepository;
    @Autowired
    AccountService accountService;
    @Test
    void contextLoads() {
        when(accountRepository.findById(1L)).thenReturn(Data.createAccount001());
        when(accountRepository.findById(2L)).thenReturn(Data.createAccount002());
        when(bankRepository.findById(1L)).thenReturn(Data.createBank());

        BigDecimal balanceSource = accountService.checkBalance(1L);
        BigDecimal balanceDestination = accountService.checkBalance(2L);
        assertEquals("1000", balanceSource.toPlainString());
        assertEquals("2000", balanceDestination.toPlainString());

        accountService.transfer(1L, 2L, new BigDecimal("100"), 1L);
        balanceSource = accountService.checkBalance(1L);
        balanceDestination = accountService.checkBalance(2L);

        assertEquals("900", balanceSource.toPlainString());
        assertEquals("2100", balanceDestination.toPlainString());

        int totalTransfer = accountService.checkTotalTransfers(1L);
        assertEquals(1, totalTransfer);

        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(3)).findById(2L);
        verify(accountRepository, times(2)).save(any(Account.class));

        verify(bankRepository, times(2)).findById(1L);
        verify(bankRepository).save(any(Bank.class));

        verify(accountRepository, times(6)).findById(anyLong());
        verify(accountRepository, never()).findAll();
    }
    @Test
    void contextLoads2() {
        when(accountRepository.findById(1L)).thenReturn(Data.createAccount001());
        when(accountRepository.findById(2L)).thenReturn(Data.createAccount002());
        when(bankRepository.findById(1L)).thenReturn(Data.createBank());

        BigDecimal balanceSource = accountService.checkBalance(1L);
        BigDecimal balanceDestination = accountService.checkBalance(2L);
        assertEquals("1000", balanceSource.toPlainString());
        assertEquals("2000", balanceDestination.toPlainString());

        assertThrows(InsufficientFundsException.class, () -> {
            accountService.transfer(1L, 2L, new BigDecimal("1200"), 1L);
        });
        balanceSource = accountService.checkBalance(1L);
        balanceDestination = accountService.checkBalance(2L);

        assertEquals("1000", balanceSource.toPlainString());
        assertEquals("2000", balanceDestination.toPlainString());

        int totalTransfer = accountService.checkTotalTransfers(1L);
        assertEquals(0, totalTransfer);

        verify(accountRepository, times(3)).findById(1L);
        verify(accountRepository, times(2)).findById(2L);
        verify(accountRepository, never()).save(any(Account.class));

        verify(bankRepository, times(1)).findById(1L);
        verify(bankRepository, never()).save(any(Bank.class));

        verify(accountRepository, times(5)).findById(anyLong());
        verify(accountRepository, never()).findAll();
    }

    @Test
    void contextLoads3() {
        when(accountRepository.findById(1L)).thenReturn(Data.createAccount001());

        Account account1 = accountService.findAccountById(1L);
        Account account2 = accountService.findAccountById(1L);

        assertSame(account1, account2);
        assertEquals(account1.getPerson(), account2.getPerson());
        assertEquals("Angel", account1.getPerson());
        assertEquals("Angel", account2.getPerson());

        verify(accountRepository, times(2)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<Account> data = Arrays.asList(Data.createAccount001().orElseThrow(), Data.createAccount002().orElseThrow());
        when(accountRepository.findAll()).thenReturn(data);

        List<Account> accounts = accountService.findAll();

        assertFalse(accounts.isEmpty());
        assertEquals(accounts.size(), data.size());
        assertTrue(accounts.contains(Data.createAccount001().orElseThrow()));

        verify(accountRepository).findAll();
    }

    @Test
    void testSaveAccount() {
        Account newAccount = new Account(null, "Antonio", new BigDecimal("3000"));
        when(accountRepository.save(any(Account.class))).then(invocationOnMock -> {
            Account a = invocationOnMock.getArgument(0);
            a.setId(3L);
            return a;
        });

        Account savedAccount = accountService.save(newAccount);
        assertEquals("Antonio", savedAccount.getPerson());
        assertEquals(3L, savedAccount.getId());

        verify(accountRepository).save(any(Account.class));
    }
}