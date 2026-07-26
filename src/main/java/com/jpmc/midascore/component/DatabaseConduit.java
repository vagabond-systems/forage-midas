package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IncentiveConduct incentiveConduct;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository, IncentiveConduct incentiveConduct) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveConduct = incentiveConduct;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void processTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord receiver = userRepository.findById(transaction.getRecipientId());
        if(sender == null || receiver == null) {
            return;
//            throw new IllegalArgumentException("Sender or receiver not found in the database.");
        }
        /// check sender has enough balance to send to receiver
        if(sender.getBalance() < transaction.getAmount()) {
            return;
//            throw new IllegalArgumentException("Sender does not have enough balance to send the transaction.");
        }
        // get incentive from incentive service which is running on localhost:8080/incentive
        Incentive incentive = incentiveConduct.getIncentive(transaction);
        float incentiveAmount = incentive.getAmount();
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        receiver.setBalance(receiver.getBalance() + transaction.getAmount() + incentiveAmount);
        userRepository.save(sender);
        userRepository.save(receiver);

        TransactionRecord transactionRecord = new TransactionRecord(sender, receiver, transaction.getAmount(),incentiveAmount);
        transactionRepository.save(transactionRecord);
    }

}
