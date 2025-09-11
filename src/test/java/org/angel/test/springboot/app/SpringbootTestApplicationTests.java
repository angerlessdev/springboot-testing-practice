package org.angel.test.springboot.app;

import org.angel.test.springboot.app.models.Account;
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
        when(accountRepository.findById(1L)).thenReturn(Data.ACCOUNT_001);
        when(accountRepository.findById(2L)).thenReturn(Data.ACCOUNT_002);
        when(bankRepository.findById(1L)).thenReturn(Data.BANK);

        BigDecimal balanceSource = accountService.checkBalance(1L);
        BigDecimal balanceDestination = accountService.checkBalance(2L);
        assertEquals("1000", balanceSource.toPlainString());
        assertEquals("2000", balanceDestination.toPlainString());

        //Account account_001 = accountService.findAccountById(1L);
        //assertEquals("Angel", account_001.getPerson());
        accountService.transfer(1L, 2L, new BigDecimal("100"), 1L);
        balanceSource = accountService.checkBalance(1L);
        balanceDestination = accountService.checkBalance(2L);

        assertEquals("900", balanceSource.toPlainString());
        assertEquals("2100", balanceDestination.toPlainString());
    }

}
