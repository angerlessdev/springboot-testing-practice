package org.angel.test.springboot.app;

import org.angel.test.springboot.app.models.Account;
import org.angel.test.springboot.app.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
public class IntegrationJpaTest {
    @Autowired
    AccountRepository accountRepository;
    @Test
    void testFindAccountById() {
        Optional<Account> account = accountRepository.findById(1L);
        assertTrue(account.isPresent());
        assertEquals("Angel", account.orElseThrow().getPerson());
    }

    @Test
    void testFindByPerson() {
        Optional<Account> account = accountRepository.findByPerson("Angel");
        assertTrue(account.isPresent());
        assertEquals("Angel", account.orElseThrow().getPerson());
        assertEquals("1000.00", account.get().getBalance().toPlainString());
    }
    @Test
    void testFindByPersonThrowException() {
        Optional<Account> account = accountRepository.findByPerson("Antonio");
        assertThrows(NoSuchElementException.class, account::orElseThrow);
        assertFalse(account.isPresent());
    }

    @Test
    void testFindAll() {
        List<Account> accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());
        assertEquals(2, accounts.size());
    }

    @Test
    void testSave() {
        // Given
        Account newAccount = new Account(null, "Antonio", new BigDecimal("3000"));
        accountRepository.save(newAccount);

        // When
        Account actual = accountRepository.findByPerson("Antonio").orElseThrow();

        // Then
        assertSame(newAccount, actual);
        assertEquals("Antonio", actual.getPerson());
        assertEquals("3000", actual.getBalance().toPlainString());
        assertEquals(3, actual.getId());
    }
    @Test
    void testUpdate() {
        // Given
        Account newAccount = new Account(null, "Antonio", new BigDecimal("3000"));

        // When
        Account actual = accountRepository.save(newAccount);

        // Then
        assertSame(newAccount, actual);
        assertEquals("Antonio", actual.getPerson());
        assertEquals("3000", actual.getBalance().toPlainString());

        actual.setBalance(new BigDecimal("4000"));
        Account newActual = accountRepository.save(actual);

        assertEquals("Antonio", actual.getPerson());
        assertEquals("4000", actual.getBalance().toPlainString());
    }

    @Test
    void testDelete() {
        Account deleteAccount = accountRepository.findByPerson("Angel").orElseThrow();
        assertEquals("Angel", deleteAccount.getPerson());

        accountRepository.delete(deleteAccount);

        assertThrows(NoSuchElementException.class, () -> accountRepository.findByPerson("Angel").orElseThrow());
        assertEquals(1, accountRepository.findAll().size());
    }

    @Test
    void debugDatabase() {
        List<Account> allAccounts = accountRepository.findAll();
        System.out.println("Total accounts: " + allAccounts.size());

        for (Account acc : allAccounts) {
            System.out.println("ID: " + acc.getId() +
                    ", Person: '" + acc.getPerson() + "'" +
                    ", Balance: " + acc.getBalance());
        }
    }
}
