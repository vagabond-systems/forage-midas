package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

@SpringBootTest
class TaskOneTests {
    static final Logger logger = LoggerFactory.getLogger(TaskOneTests.class);

    @Configuration
    static class Config {
        Config() {
            System.out.println("Config");
        }
    }

    @Test
    void task_one_verifier() throws InterruptedException {
        Thread.sleep(2000);
        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
        logger.info("Congrats! It looks like your application booted without issue");
        logger.info("submit the following output to complete the task (include begin and end output denotations)");
        StringBuilder output = new StringBuilder("\n").append("---begin output ---").append("\n");
        for (int i = 0; i < 10; i++) {
            output.append(String.valueOf((int) Math.floor(Math.pow(i, i))));
        }
        output.append("\n").append("---end output ---");
        logger.info(output.toString());

    }

}
