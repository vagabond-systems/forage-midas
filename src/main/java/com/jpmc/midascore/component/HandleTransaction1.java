package com.jpmc.midascore.component;

import org.springframework.stereotype.Component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

@Component
public class HandleTransaction1 {
    private final TransactionRepository transactionRecordInterface;
    private final UserRepository userRepository;

    public HandleTransaction1(TransactionRepository transactionRecordInterface, UserRepository userRepository) {
        this.transactionRecordInterface = transactionRecordInterface;
        this.userRepository = userRepository;
    }

    public UserRecord getUser(long id) {
        return userRepository.findById(id);
    }
    
    public boolean isValid(Transaction transaction) {
        UserRecord sender = getUser(transaction.getSenderId());
        UserRecord recipient = getUser(transaction.getRecipientId());

        if (sender.getBalance() < transaction.getAmount() && sender != null && recipient != null) {
            return true;
        }
        return false;
    }
    
    public void transactionHandler(Transaction transaction) {
        if (isValid(transaction)) {
            UserRecord sender = getUser(transaction.getSenderId());
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            userRepository.save(sender);

            UserRecord recipient = getUser(transaction.getRecipientId());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount());
            userRepository.save(recipient);

            TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount());
            transactionRecordInterface.save(transactionRecord);
        }
        
    }
}
