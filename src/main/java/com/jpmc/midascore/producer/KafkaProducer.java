package com.jpmc.midascore.producer;

import com.jpmc.midascore.foundation.Transaction;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component

public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final String topic;
    private final KafkaTemplate<Object, String> kafkaTemplate;
    private final String deadLetterTopic;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic,
                         @Value("${general.kafka-dead-letter-topic}") String deadLetterTopic,
                         KafkaTemplate<Object, String> kafkaTemplate) {
       log.info("KafkaProducer created with topic: " + topic + " and dead letter topic: " + deadLetterTopic);
        this.topic = topic;
        this.deadLetterTopic = deadLetterTopic;
        this.kafkaTemplate = kafkaTemplate;
    }


    public void send(String transactionLine) {
        String[] transactionData = transactionLine.split(", ");
        Transaction transaction = new Transaction(Long.parseLong(transactionData[0]), Long.parseLong(transactionData[1]), Float.parseFloat(transactionData[2]));
        log.info("Sending transaction: " + transaction);
        //kafkaTemplate.send(topic, transactionLine);
        kafkaTemplate.send(topic, transaction.toString());
    }

    public void sendToDeadLetter(String transactionLine) {
        kafkaTemplate.send(deadLetterTopic, transactionLine);
    }
}
