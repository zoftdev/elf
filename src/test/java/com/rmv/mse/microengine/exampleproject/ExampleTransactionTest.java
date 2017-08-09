package com.rmv.mse.microengine.exampleproject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by zoftdev on 8/8/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleTransactionTest {
    @Test
    public void example_log_inside() throws Exception {
        exampleTransaction.example_log_inside("zoftdev",null);
    }

    @Autowired
    private ExampleService service;

    @Autowired
    ExampleTransaction exampleTransaction;

    @Test
    public void example_hello_world() throws Exception {
        exampleTransaction.example_hello_world();
    }


    @Test
    public void example_devOnlyActivity() {

        service.doActivity("hlex","password");
        service.doActivity(null,"password");

    }

}