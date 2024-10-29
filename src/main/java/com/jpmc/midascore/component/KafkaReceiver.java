package com.jpmc.midascore.component;

import org.springframework.kafka.annotation.KafkaListener;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Component;

@Component
public class KafkaReceiver {
    private final HandleTransaction1 handler;

    public KafkaReceiver(HandleTransaction1 handler) {
        this.handler = handler;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId="midas")
    public void listen(Transaction transaction) {
        handler.transactionHandler(transaction);
    }
}
