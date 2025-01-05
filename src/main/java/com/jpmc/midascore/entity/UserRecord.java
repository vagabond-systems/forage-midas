package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false)
    private BigDecimal amount;

    protected TransactionRecord() {
    }

    public TransactionRecord(User sender, User recipient, BigDecimal amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("Transaction[id=%d, sender='%s', recipient='%s', amount='%s']",
                             id, sender.getName(), recipient.getName(), amount);
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
