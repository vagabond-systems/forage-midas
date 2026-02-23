package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IncentiveApiClient incentiveApiClient;

    public DatabaseConduit(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            IncentiveApiClient incentiveApiClient
    ) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveApiClient = incentiveApiClient;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender == null || recipient == null) {
            return false;
        }

        float amount = transaction.getAmount();
        if (sender.getBalance() < amount) {
            return false;
        }

        float incentive = incentiveApiClient.fetchIncentive(transaction);
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount + incentive);

        if (sender.getId().equals(recipient.getId())) {
            userRepository.save(sender);
        } else {
            userRepository.save(sender);
            userRepository.save(recipient);
        }

        transactionRepository.save(new TransactionRecord(sender, recipient, amount, incentive));
        return true;
    }
}
