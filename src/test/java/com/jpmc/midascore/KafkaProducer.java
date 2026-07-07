package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class KafkaProducer {
    private final String topic;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic, KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String transactionLine) {
        String[] transactionData = transactionLine.split(", ");
        Transaction transaction = new Transaction(
                Long.parseLong(transactionData[0]),
                Long.parseLong(transactionData[1]),
                Float.parseFloat(transactionData[2]));

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                kafkaTemplate.send(topic, transaction).get(10, TimeUnit.SECONDS);
                return;
            } catch (Exception ex) {
                if (attempt == 3) {
                    throw new IllegalStateException("Unable to publish transaction to Kafka topic " + topic, ex);
                }
                try {
                    Thread.sleep(500L * attempt);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Interrupted while retrying Kafka send", interruptedException);
                }
            }
        }
    }
}