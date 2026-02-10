package com.jpmc.midascore.entity;

import com.jpmc.midascore.foundation.Balance;
import jakarta.persistence.*;

@Entity
public class UserRecord {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Balance balance;

    protected UserRecord() {
    }

    public UserRecord(String name, Balance balance) {
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, name='%s', balance=%s]",
                id, name, balance
        );
    }
}
