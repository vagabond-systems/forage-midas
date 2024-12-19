//package com.jpmc.midascore;
//
//import com.jpmc.midascore.foundation.Transaction;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaProducer {
//    private final String topic;
//    private final KafkaTemplate<String, Transaction> kafkaTemplate;
//
//    public KafkaProducer(@Value("${general.kafka-topic}") String topic, KafkaTemplate<String, Transaction> kafkaTemplate) {
//        this.topic = topic;
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void send(String transactionLine) {
//        String[] transactionData = transactionLine.split(", ");
//        kafkaTemplate.send(topic, new Transaction(Long.parseLong(transactionData[0]), Long.parseLong(transactionData[1]), Float.parseFloat(transactionData[2])));
//    }
//}