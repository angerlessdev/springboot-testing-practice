package org.angel.test.springboot.app.models;

public class Bank {
    private Long id;
    private String name;
    private Integer totalTransfers;

    public Bank() {
    }

    public Bank(Long id, String name, Integer totalTransfers) {
        this.id = id;
        this.name = name;
        this.totalTransfers = totalTransfers;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTotalTransfers() {
        return totalTransfers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalTransfers(Integer totalTransactions) {
        this.totalTransfers = totalTransactions;
    }
}
