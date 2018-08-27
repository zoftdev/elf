package com.rmv.mse.microengine.exampleproject;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("embed")
@SpringBootTest
@RunWith(SpringRunner.class)
public class ConfigKafkaEmbededTest {
    @Value("${elf.kafka.bootstrap-servers}")
    String testvalue;


    @ClassRule
    public static KafkaEmbedded kafkaEmbedded=new KafkaEmbedded(1,true,"topic1");
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired ConfigKafkaEmbededTest.Listener listener;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testConfig(){
        logger.info("bootstrap: {}",testvalue);
    }

    @Test
    public void testSendRecevice() throws InterruptedException, ExecutionException {

        ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send("topic1", "data");
        SendResult<String, String> sendResult = result.get();
        logger.info("send result: {}",sendResult.getProducerRecord());
        assertThat(
         listener.countDownLatch.await(10, TimeUnit.SECONDS),
                is(true));

    }

    @Profile("embed")
    @TestConfiguration
    public static class ListenerConfig{
        @Bean
        public Listener listener(){
            return new Listener();
        }

    }

    public  static class Listener{

        public CountDownLatch countDownLatch=new CountDownLatch(1);
        private final Logger logger = LoggerFactory.getLogger(getClass());
        @KafkaListener(topics={"topic1"})
        public void listen(String data){
            logger.info("data: {}",data);
            countDownLatch.countDown();
        }

    }
}
