package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final UserRepository userRepository;

    @GetMapping("/balance")
    public Balance getBalance(long userId) {
        UserRecord user =  userRepository.findById(userId);
        float balance = (user != null) ? user.getBalance() : 0;
        return new Balance(userId, balance);

    }
}