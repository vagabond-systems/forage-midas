package com.jpmc.midascore.foundation;

import com.jpmc.midascore.entity.UserRecord;
import jakarta.persistence.*;
import org.apache.catalina.User;

@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_Id")
    private UserRecord   sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_Id")
    private UserRecord  recipient;

    private float balance;

    private float incentives;


    public Transactions(UserRecord sender, UserRecord recipient, float balance, float incentives) {
        this.sender = sender;
        this.recipient = recipient;
        this.balance = balance;
        this.incentives = incentives;
    }

    public Transactions() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getIncentives() {
        return incentives;


    }
    public void setIncentives(float incentives) {
        this.incentives = incentives;
    }
}

