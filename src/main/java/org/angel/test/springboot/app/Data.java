package org.angel.test.springboot.app;

import org.angel.test.springboot.app.models.Account;
import org.angel.test.springboot.app.models.Bank;

import java.math.BigDecimal;

public class Data {
    // public static final Account ACCOUNT_001 = new Account(1L, "Angel", new BigDecimal("1000"));
    // public static final Account ACCOUNT_002 = new Account(2L, "Estrella", new BigDecimal("2000"));
    // public static final Bank BANK = new Bank(1L, "The financial bank", 0);

    public static Account createAccount001() {
        return new Account(1L, "Angel", new BigDecimal("1000"));
    }
    public static Account createAccount002() {
        return new Account(2L, "Estrella", new BigDecimal("2000"));
    }
    public static Bank createBank() {
        return new Bank(1L, "The financial bank", 0);
    }
}
