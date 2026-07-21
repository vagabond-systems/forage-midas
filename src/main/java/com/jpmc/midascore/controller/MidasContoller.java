package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MidasContoller {

    private final UserRepository userRepository;


    public MidasContoller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam long userId) {
        UserRecord userRecord = userRepository.findById(userId);

        if (userRecord == null) {
            return new Balance(0);
        }
        return new Balance(userRecord.getBalance());
    }
}
