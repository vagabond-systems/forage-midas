package com.jpmc.midascore.Service;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentives;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Transactions;
import com.jpmc.midascore.repository.TransactionsRepository;

import com.jpmc.midascore.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionsRepository transactionsRepository;
    private final RestTemplate restTemplate;

    public TransactionService (UserRepository userRepository, TransactionsRepository transactionsRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.transactionsRepository = transactionsRepository;
        this.restTemplate = restTemplate;
    }

    public void sendTransaction(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());

        if (sender == null) {
            return;
        }

        UserRecord reciepinet = userRepository.findById(transaction.getRecipientId());

        if (reciepinet == null) {
            return;
        }


        if (sender.getBalance() < transaction.getAmount()) {
            return;
        }

        Incentives incentives = restTemplate.postForObject("http://localhost:8080/incentive", transaction, Incentives.class);


        sender.setBalance(sender.getBalance() - transaction.getAmount());
        reciepinet.setBalance(reciepinet.getBalance() + transaction.getAmount() + incentives.getAmount());

        Transactions transactions = new Transactions(sender, reciepinet, transaction.getAmount(), incentives.getAmount());
        userRepository.save(sender);
        userRepository.save(reciepinet);
        transactionsRepository.save(transactions);





    }


}
