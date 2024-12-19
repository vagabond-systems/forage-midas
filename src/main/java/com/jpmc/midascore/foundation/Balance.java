package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Balance {
    private long userId;
    private float amount;

    public Balance() {
    }

    public Balance(long userId, float amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Balance {amount=" + amount + "}";
    }
}