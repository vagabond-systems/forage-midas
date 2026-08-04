package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    public DatabaseConduit(UserRepository userRepository,
                           TransactionRepository transactionRepository,
                           RestTemplateBuilder builder) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.restTemplate = builder.build();
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void save(TransactionRecord transactionRecord) {
        transactionRepository.save(transactionRecord);
    }

    public void process(Transaction transaction) {

        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender == null || recipient == null) {
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            return;
        }

        // Call Incentive API
        String url = "http://localhost:8080/incentive";
        Incentive incentive =
                restTemplate.postForObject(url, transaction, Incentive.class);

        float incentiveAmount = 0.0f;
        if (incentive != null) {
            incentiveAmount = incentive.getAmount();
        }

        // Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());

        recipient.setBalance(
                recipient.getBalance()
                        + transaction.getAmount()
                        + incentiveAmount
        );

        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord transactionRecord =
                new TransactionRecord(sender, recipient, transaction.getAmount());

        transactionRepository.save(transactionRecord);
    }
}