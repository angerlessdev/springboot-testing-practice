package org.angel.test.springboot.app;

import org.angel.test.springboot.app.exceptions.InsufficientFundsException;
import org.angel.test.springboot.app.models.Account;
import org.angel.test.springboot.app.models.Bank;
import org.angel.test.springboot.app.repositories.AccountRepository;
import org.angel.test.springboot.app.repositories.BankRepository;
import org.angel.test.springboot.app.services.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {
    @Mock
    AccountRepository accountRepository;
    @Mock
    BankRepository bankRepository;
    @InjectMocks
    AccountServiceImpl accountService;
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
        verify(accountRepository, times(2)).update(any(Account.class));

        verify(bankRepository, times(2)).findById(1L);
        verify(bankRepository).update(any(Bank.class));
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
        verify(accountRepository, never()).update(any(Account.class));

        verify(bankRepository, times(1)).findById(1L);
        verify(bankRepository, never()).update(any(Bank.class));
    }
}
