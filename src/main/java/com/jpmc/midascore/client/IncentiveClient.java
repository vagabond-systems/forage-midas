package com.jpmc.midascore.client;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(name = "incentiveClient", url = "http://localhost:8080")
public interface IncentiveClient {

    @PostMapping("/incentive")
    Incentive getIncentive(Transaction transaction);
}