package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.service.IncentiveService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IncentiveService incentiveService;
    public TransactionListener(UserRepository userRepository, TransactionRepository transactionRepository, IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveService = incentiveService;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {
       UserRecord sender = userRepository.findById(transaction.getSenderId());
       UserRecord receiver = userRepository.findById(transaction.getRecipientId());

       float amount = transaction.getAmount();
       if(sender == null) {
           return;
       }
       if(receiver == null) {
           return;
       }
       if (amount > sender.getBalance()) {
           return;
       }
       Incentive incentive = incentiveService.getIncentive(transaction);
       sender.setBalance(sender.getBalance() - amount);
       receiver.setBalance(receiver.getBalance() + amount + incentive.getAmount());

       userRepository.save(sender);
       userRepository.save(receiver);

        TransactionRecord record = new TransactionRecord(sender,receiver,amount,incentive.getAmount());
        transactionRepository.save(record);
    }
}
