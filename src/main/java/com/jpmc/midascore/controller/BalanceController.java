package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class BalanceController {

    private final UserRepository userRepo;

    public BalanceController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam long userId) {
        Optional<UserRecord> userOpt = userRepo.findById(userId);

        if (userOpt.isPresent()) {
            return new Balance(userOpt.get().getBalance());
        } else {
            return new Balance(0f);
        }
    }
}
