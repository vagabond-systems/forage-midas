package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final DatabaseConduit databaseConduit;

    public KafkaConsumer(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "midas-group"
    )
    public void listen(Transaction transaction) {

        UserRecord sender = databaseConduit.findUser(transaction.getSenderId());

        System.out.println(sender.getName());
        UserRecord recipient = databaseConduit.findUser(transaction.getRecipientId());
        System.out.println(sender.getBalance());


        System.out.println(recipient.getName());
        System.out.println(recipient.getBalance());

        // Validate sender and recipient
        if (sender == null || recipient == null) {
            return;
        }

        // Validate balance
        if (sender.getBalance() < transaction.getAmount()) {
            return;
        }

        // Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        // Save updated users
        databaseConduit.saveUser(sender);
        databaseConduit.saveUser(recipient);

        // Record transaction
        TransactionRecord transactionRecord =
                new TransactionRecord(sender, recipient, transaction.getAmount());

        databaseConduit.saveTransaction(transactionRecord);
    }
}