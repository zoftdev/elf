package com.rmv.mse.microengine.logback2kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaLogTest {

    private final Logger logger = LoggerFactory.getLogger("test-stash");


    @Test
    public void logKafkatest(){
        MDC.put("mdc-key","mdc-value");

            logger.info("{}",new Date());


        sleep(2);


    }

    private void sleep(int sec) {
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
