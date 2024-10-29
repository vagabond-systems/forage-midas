package com.jpmc.midascore.component;

import org.springframework.kafka.annotation.KafkaListener;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenr {

    @KafkaListener(topics = "${general.kafka-topic}", groupId="midas")
    public void listen(Transaction transaction) {
        System.out.println(transaction);
    }
    
}
