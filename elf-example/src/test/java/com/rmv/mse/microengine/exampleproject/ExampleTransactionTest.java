package com.rmv.mse.microengine.exampleproject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zoftdev on 8/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleTransactionTest {
    @Test
    public void testReturnMap() throws Exception {
        exampleTransaction.testReturnMap();
    }

    @Autowired ExampleTransaction exampleTransaction;

    @AfterClass
    public static void waitKafka(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notLogResponse() throws Exception {
        exampleTransaction.notLogResponse();
    }

    @Test
    public void overrideName() throws Exception {
        exampleTransaction.overrideName();
    }

    @Test
    public void example_hello_world() throws Exception {
        exampleTransaction.example_hello_world();
    }

    @Test
    public void log_id() throws Exception {
        exampleTransaction.log_id();
    }


    @Test
    public void logging_and_pass_to_activity() throws Exception {
        exampleTransaction.logging_and_pass_to_activity();
    }

    @Test
    public void deepService() throws Exception {
        exampleTransaction.deepService();
    }

//    @Ignore
    @Test(expected = RuntimeException.class)
    public void exception() throws Exception {
        exampleTransaction.doException();

    }

   // @Ignore
    @Test(expected = RuntimeException.class)
    public void doExceptionFromService() throws Exception {
        exampleTransaction.doExceptionFromService();
    }

    @Test
    public void doThreadService() throws Exception {
        exampleTransaction.doThreadService();
    }

    @Test
    public void doOverlapFunction(){
        exampleTransaction.doOverlapFunction();
    }


    @Test
    public void testNested() throws Exception {
        exampleTransaction.nestedTransaction();
    }

}