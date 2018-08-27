package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.context.LogContext;
import com.rmv.mse.microengine.logging.context.LogContextService;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.fail;

@ActiveProfiles("embed")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class EmbedTest {
    @ClassRule
    public static KafkaEmbedded kafkaEmbedded=new KafkaEmbedded(1, true,"elf-activity-UAT","topic-test");

    private final Logger loggerStashActivity = LoggerFactory.getLogger("stash-activity");

    @Autowired
    ExampleService service;

    @Autowired
    Listener listener;

    @Autowired
    ListenerTest listenerTest;

    @Autowired
    LogContextService logContextService;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Before
    public void init(){
        logContextService.addTransactionLoggingContext(LogContext.createBasic());
        listener.countDownLatch=new CountDownLatch(1);
        listenerTest.countDownLatchTest=new CountDownLatch(1);
    }

    @Test
    public void configTest() throws InterruptedException {
        kafkaTemplate.send("topic-test","data");
        assertThat( listenerTest.countDownLatchTest.await(10, TimeUnit.SECONDS),
                is(true)
        );
    }

    @Test
    public void testActivtiyLogToKafka() throws Exception {
        loggerStashActivity.info("push data!");
        assertThat( listener.countDownLatch.await(10, TimeUnit.SECONDS), is(true));
    }


    @Test(expected = RuntimeException.class)
    public void doException() throws Exception {
        try {
            service.doException();
            fail("should throw exception");
        }

        finally {
            assertThat( listener.countDownLatch.await(10, TimeUnit.SECONDS),
                    is(true)
            );

        }

    }

    @Test
    public void doExampleLogging() throws Exception {

        service.exampleLogging("user","password");
        assertThat( listener.countDownLatch.await(10, TimeUnit.SECONDS),
                is(true)
        );



    }


    @Profile("embed")
    @TestConfiguration
    public static class ListenerConfig{
        @Bean
        public Listener listener(){
            return new Listener();
        }

        @Bean
        public ListenerTest listenerTest(){
            return new ListenerTest();
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

    public  static class ListenerTest{

        public  CountDownLatch countDownLatchTest=new CountDownLatch(1);
        private final Logger logger = LoggerFactory.getLogger(getClass());
        @KafkaListener(topics={"topic-test"})
        public void listen(String data){
            logger.info("data config-test: {}",data);
            countDownLatchTest.countDown();
        }

    }

}
