package com.jpmc.midascore;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;

public class KafkaConsumer {
    private final org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;

    public KafkaConsumer(Map<String, Object> consumerProps) {
        Properties props = new Properties();
        props.putAll(consumerProps);
        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
    }

    public void subscribe(String topic) {
        consumer.subscribe(java.util.Collections.singletonList(topic));
    }

    public void poll() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }

    public void close() {
        consumer.close();
    }

}