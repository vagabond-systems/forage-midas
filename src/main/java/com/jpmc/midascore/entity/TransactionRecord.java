package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id", nullable = false)
    private UserRecord recipient;

    @Column(name = "transaction_amount", nullable = false)
    private float amount;

    @Column(name = "incentive")
    private float incentive;

    public TransactionRecord() {
    }

    public TransactionRecord(UUID id, UserRecord sender, UserRecord recipient, float amount, float incentive) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }

    // Private constructor to prevent direct instantiation
    public TransactionRecord(Builder builder) {
        this.id = builder.id;
        this.sender = builder.sender;
        this.recipient = builder.recipient;
        this.amount = builder.amount;
        this.incentive = builder.incentive;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getIncentive() {
        return incentive;
    }

    public void setIncentive(float incentive) {
        this.incentive = incentive;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRecord that = (TransactionRecord) o;
        return Float.compare(amount, that.amount) == 0 && Float.compare(incentive, that.incentive) == 0 && Objects.equals(id, that.id) && Objects.equals(sender, that.sender) && Objects.equals(recipient, that.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, recipient, amount, incentive);
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", amount=" + amount +
                ", incentive=" + incentive +
                '}';
    }

    // Builder class
    public static class Builder {

        private UUID id;
        private UserRecord sender;
        private UserRecord recipient;
        private float amount;
        private float incentive;

        // Setters for the builder fields
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder sender(UserRecord sender) {
            this.sender = sender;
            return this;
        }

        public Builder recipient(UserRecord recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder amount(float amount) {
            this.amount = amount;
            return this;
        }

        public Builder incentive(float incentive) {
            this.incentive = incentive;
            return this;
        }

        // Build the instance
        public TransactionRecord build() {
            return new TransactionRecord(this);
        }
    }
}
