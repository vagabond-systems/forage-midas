package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveConduct {

    private final RestTemplate restTemplate;

    public IncentiveConduct(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Incentive getIncentive(Transaction transaction) {
        String url = "http://localhost:8080/incentive";
        return restTemplate.postForObject(url,transaction, Incentive.class);
    }
}
