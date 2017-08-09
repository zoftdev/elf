package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.annotation.TransactionLogging;
import com.rmv.mse.microengine.logging.model.TransactionLoggingContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by zoftdev on 8/9/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleServiceTest {
    @Test
    public void doException() throws Exception {
        service.doException();
    }

    @Test
    public void appendFieldActivityLevel() throws Exception {
        service.appendFieldActivityLevel(TransactionLoggingContext.getDummy());
    }


    @Test
    public void exampleLogging() throws Exception {
        service.exampleLogging("hlex", "password",TransactionLoggingContext.getDummy());
    }

    @Autowired
    ExampleService service;
    @Test
    public void doActivityLogResult() throws Exception {
        service.doActivityLogResult();

    }

}