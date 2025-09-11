package org.angel.test.springboot.app.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private Long id;
    private String person;
    private BigDecimal balance;

    public Account() {
    }

    public Account(Long id, String person, BigDecimal balance) {
        this.id = id;
        this.person = person;
        this.balance = balance;
    }

    public void debit(BigDecimal amount) {
        BigDecimal newBalance = balance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new RuntimeException("Insufficient funds in the account");
        this.balance = newBalance;
    }
    public void credit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getPerson() {
        return person;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(person, account.person) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, balance);
    }
}
