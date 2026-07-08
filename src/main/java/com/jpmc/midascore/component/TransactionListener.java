package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.service.IncentiveService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {
    static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveService incentiveService;

    public TransactionListener(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository, IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveService = incentiveService;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(ConsumerRecord<String, Transaction> record) {
        Transaction transaction = record.value();
        logger.info("Received transaction: {}", transaction);

        // Validate sender exists
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) {
            logger.warn("Invalid transaction: sender {} does not exist", transaction.getSenderId());
            return;
        }

        // Validate recipient exists
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) {
            logger.warn("Invalid transaction: recipient {} does not exist", transaction.getRecipientId());
            return;
        }

        // Validate sender has enough balance
        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Invalid transaction: sender {} has insufficient balance (has: {}, needs: {})",
                    sender.getId(), sender.getBalance(), transaction.getAmount());
            return;
        }

        // Transaction is valid - process it
        logger.info("Valid transaction: processing");

        // Call incentive API
        Incentive incentive = incentiveService.getIncentive(transaction);
        logger.info("Incentive received: {}", incentive.getAmount());

        // Save transaction to database with incentive
        TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount(), incentive.getAmount());
        transactionRecordRepository.save(transactionRecord);

        // Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentive.getAmount());

        userRepository.save(sender);
        userRepository.save(recipient);

        logger.info("Transaction processed successfully. New balances - sender: {}, recipient: {}",
                sender.getBalance(), recipient.getBalance());
    }
}
