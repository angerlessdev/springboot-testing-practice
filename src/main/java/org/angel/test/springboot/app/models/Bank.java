package org.angel.test.springboot.app.models;

public class Bank {
    private Long id;
    private String name;
    private Integer totalTransactions;

    public Bank() {
    }

    public Bank(Long id, String name, Integer totalTransactions) {
        this.id = id;
        this.name = name;
        this.totalTransactions = totalTransactions;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTotalTransactions() {
        return totalTransactions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
}
