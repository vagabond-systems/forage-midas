package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KafkaConsumer {

    private final DatabaseConduit database;
    private final RestTemplate restTemplate;

    public KafkaConsumer(DatabaseConduit database,
                         RestTemplate restTemplate) {

        this.database = database;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(
            topics="${general.kafka-topic}",
            groupId="midas-group")
    public void receive(Transaction transaction) {

        UserRecord sender = database.findUser(transaction.getSenderId());
        UserRecord recipient = database.findUser(transaction.getRecipientId());

        if (sender == null || recipient == null) {
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            return;
        }

        float incentiveAmount = 0f;
        try {
            Incentive incentive = restTemplate.postForObject(
                    "http://localhost:8080/incentive",
                    transaction,
                    Incentive.class);

            if (incentive != null) {
                incentiveAmount = incentive.getAmount();
            }
        } catch (Exception ignored) {
            incentiveAmount = 0f;
        }

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        database.saveUser(sender);
        database.saveUser(recipient);

        database.saveTransaction(new TransactionRecord(
                sender,
                recipient,
                transaction.getAmount(),
                incentiveAmount));
    }
}