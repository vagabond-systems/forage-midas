package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate;

    // Constructor injection
    public TransactionService(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "transaction-group")
    public void processTransaction(Transaction transaction) {

        // Fetch sender and recipient from the repository by ID
        UserRecord sender = userRepository.findById(transaction.getSenderId()).orElse(null);
        UserRecord recipient = userRepository.findById(transaction.getRecipientId()).orElse(null);

        //System.out.println("Users from service file: " + userRepository.findAll());
        //System.out.println("Transaction sender id Works: " + transaction.getSenderId());
        //System.out.println("Sender object: " + sender);

        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {

            // If transaction is valid, record it
            TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount());
            transactionRecordRepository.save(transactionRecord);

            // Update balance of sender
            sender.setBalance(sender.getBalance() - transaction.getAmount());

            // Try and catch when calling the api
            float incentiveAmount = 0;
            try{
                incentiveAmount = getIncentiveApi(transaction).getAmount();
            }
            catch
            (Exception e) {
                System.out.println("Error occurred while calling Incentive Api " + e);
            }
            // Update balance of recipient
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + Math.max(0, incentiveAmount));

            // Persist updated users with their new balances
            userRepository.save(sender);
            userRepository.save(recipient);
        } else {
            // If not valid, discard the transaction (no changes to database)
            System.out.println("Transaction discarded: " + transaction);
        }
    }

    public Incentive getIncentiveApi(Transaction transaction){
        String url = "http://localhost:8080/incentive";
        return restTemplate.postForObject(url, transaction, Incentive.class );
    }

}
