package com.jpmc.midascore.listener;

import com.jpmc.midascore.Service.TransactionService;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class Listener {
    private final TransactionService transactionService;

    public Listener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(@Payload Transaction transaction) {
        transactionService.sendTransaction(transaction);






    }
}
