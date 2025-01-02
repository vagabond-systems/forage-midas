package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    private float amount;

    public TransactionRecord() {
    }

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", sender=" + sender.getId() + // Assuming UserRecord has getId() method
                ", recipient=" + recipient.getId() + // Assuming UserRecord has getId() method
                ", amount=" + amount +
                '}';
    }

}
