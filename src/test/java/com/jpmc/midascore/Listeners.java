package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Listeners {

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "transaction-group")
    public void listen(Transaction transaction) {
        System.out.println("Received Transaction: " + transaction);
    }
}