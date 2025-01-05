package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float balance;

    @OneToMany(mappedBy = "sender")
    private List<TransactionRecord> sentTransactions;

    @OneToMany(mappedBy = "recipient")
    private List<TransactionRecord> receivedTransactions;

    protected User() {
    }

    public User(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s', balance='%f']", id, name, balance);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    // Add getters and setters for transactions if needed
    public List<TransactionRecord> getSentTransactions() {
        return sentTransactions;
    }

    public List<TransactionRecord> getReceivedTransactions() {
        return receivedTransactions;
    }
}
