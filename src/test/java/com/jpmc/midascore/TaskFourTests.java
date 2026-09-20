package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskFourTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void task_four_verifier() throws InterruptedException {
        // Mock the restTemplate to return zero incentive for any request to the incentive service
        Mockito.when(restTemplate.postForObject(
                ArgumentMatchers.eq("http://localhost:8080/incentive"),
                ArgumentMatchers.any(),
                ArgumentMatchers.eq(Incentive.class)))
                .thenReturn(new Incentive(0.0f));

        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }
        // Wait for messages to be processed
        Thread.sleep(2000);

        // Find Wilbur's user record and assert his balance
        UserRecord wilbur = userRepository.findByName("wilbur");
        Assertions.assertNotNull(wilbur);
        // Expected balance after all transactions with zero incentive
        Assertions.assertEquals(3089.42f, wilbur.getBalance(), 0.01f);
    }
}