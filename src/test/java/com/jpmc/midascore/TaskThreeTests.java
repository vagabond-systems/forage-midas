package com.jpmc.midascore;

import com.jpmc.midascore.component.FileLoader;
import com.jpmc.midascore.component.UserPopulator;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;  // Assuming this repository is set up


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_three_verifier() throws InterruptedException {
        userPopulator.populate();

        //userRepository.findAll().forEach(user -> System.out.println("Users from test file: " + user));

        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }
        Thread.sleep(2000);


        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("use your debugger to find out what waldorf's balance is after all transactions are processed");
        logger.info("kill this test once you find the answer");

        // Print Waldorf's final balance after all transactions are processed
        UserRecord waldorf = userRepository.findByName("waldorf");  // Assuming you have a method to find by name
        if (waldorf != null) {
            System.out.println("Final balance for Waldorf: " + waldorf.getBalance());
        } else {
            System.out.println("Waldorf not found in the database.");
        }

        while (true) {
            Thread.sleep(20000);
            logger.info("...");
        }
    }
}
