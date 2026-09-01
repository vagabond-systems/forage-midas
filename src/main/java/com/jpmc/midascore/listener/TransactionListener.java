package com.jpmc.midascore.listener;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRecordRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Component
public class TransactionListener {

    private final UserRepository userRepo;
    private final TransactionRecordRepository txRepo;


    public TransactionListener(UserRepository userRepo, TransactionRecordRepository txRepo) {
        this.userRepo = userRepo;
        this.txRepo = txRepo;
    }
    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-group")
    public void listen(Transaction transaction) {
        Optional<UserRecord> senderOpt = userRepo.findById(transaction.getSenderId());
        Optional<UserRecord> recipientOpt = userRepo.findById(transaction.getRecipientId());

        if (senderOpt.isPresent() && recipientOpt.isPresent()) {
            UserRecord sender = senderOpt.get();
            UserRecord recipient = recipientOpt.get();

            if (sender.getBalance() >= transaction.getAmount()) {
                // Deduct from sender
                sender.setBalance(sender.getBalance() - transaction.getAmount());

                // Call Incentive API
                Incentive incentive = restTemplate.postForObject(
                        "http://localhost:8080/incentive", transaction, Incentive.class);

                float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0f;

                // Add to recipient
                recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

                // Save users
                userRepo.save(sender);
                userRepo.save(recipient);

                // Record transaction with incentive
                TransactionRecord record = new TransactionRecord();
                record.setSender(sender);
                record.setRecipient(recipient);
                record.setAmount(transaction.getAmount());
                record.setIncentive(incentiveAmount);
                txRepo.save(record);

                System.out.println("✅ Transaction + incentive recorded: " + record);
            }
        }
    }

}