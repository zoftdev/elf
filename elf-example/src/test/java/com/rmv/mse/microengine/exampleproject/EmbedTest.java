package com.rmv.mse.microengine.exampleproject;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.fail;

@ActiveProfiles("embed")
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmbedTest {
    @ClassRule
    public static KafkaEmbedded kafkaEmbedded=new KafkaEmbedded(1, true,"elf-activity-UAT");

    @Autowired
    ExampleService service;

    @Autowired
    Listener listener;


    //todo Fix test
    @Test(expected = RuntimeException.class)
    public void doException() throws Exception {
        try {
            service.doException();
            fail("should throw exception");
        }finally {
            assertThat( listener.countDownLatch.await(5, TimeUnit.SECONDS),
                    is(true)
            );

        }

    }


    @TestConfiguration
    public static class ListenerConfig{
        @Bean
        public Listener listener(){
            return new Listener();
        }

    }

    public  static class Listener{

        public  CountDownLatch countDownLatch=new CountDownLatch(1);
        private final Logger logger = LoggerFactory.getLogger(getClass());
        @KafkaListener(topics={"elf-activity-${elf.zone}"})
        public void listen(String data){
            logger.info("data: {}",data);
            countDownLatch.countDown();
        }

    }


}
