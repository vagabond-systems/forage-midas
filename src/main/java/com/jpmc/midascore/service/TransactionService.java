package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    // Constructor injection
    public TransactionService(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;

    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "transaction-group")
    public void processTransaction(Transaction transaction) {

        // Fetch sender and recipient from the repository by ID
        UserRecord sender = userRepository.findById(transaction.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);

        //System.out.println("Users from service file: " + userRepository.findAll());
        //System.out.println("Transaction SENDER_ID Works: " + transaction.getSenderId());
        //System.out.println("Sender object: " + sender);

        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {

            // If transaction is valid, record it
            TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount());
            transactionRecordRepository.save(transactionRecord);

            // Update balances of sender and recipient
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount());

            // Persist updated users with their new balances
            userRepository.save(sender);
            userRepository.save(recipient);
        } else {
            // If not valid, discard the transaction (no changes to database)
            System.out.println("Transaction discarded: " + transaction);
        }


        // Print Waldorf's final balance after all transactions are processed
        UserRecord waldorf = userRepository.findByName("waldorf");  // Assuming you have a method to find by name
        if (waldorf != null) {
            System.out.println("Final balance for Waldorf: " + waldorf.getBalance());
        } else {
            System.out.println("Waldorf not found in the database.");
        }
    }
}
