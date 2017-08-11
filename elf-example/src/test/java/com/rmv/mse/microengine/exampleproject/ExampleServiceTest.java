package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.logging.context.LogContextService;
import com.rmv.mse.microengine.logging.logging.context.LogContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zoftdev on 8/9/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleServiceTest {
    @Test
    public void stackActivity() throws Exception {
        service.stackActivity();
    }

    @Test
    public void doOverrideName() throws Exception {

        service.doOverrideName();
    }

    @Autowired
    LogContextService logContextService;

    @Before
    public void init(){
        logContextService.addTransactionLoggingContext(LogContext.createBasic());
    }

    @Autowired
    ExampleService service;
    @Test
    public void doActivityLogResult() throws Exception {
        service.doActivityLogResult();

    }

    @Ignore
    @Test
    public void doException() throws Exception {
        service.doException();
    }

    @Test
    public void appendFieldActivityLevel() throws Exception {
        service.appendFieldActivityLevel();
    }


    @Test
    public void exampleLogging() throws Exception {
        service.exampleLogging("hlex", "password");
    }

    @Test
    public void doActivityNotLogResponse(){
        service.doActivityNotLogResponse();
    }

    @Test
    public void appendFieldTranLevel(){
        service.appendFieldTranLevel();
        service.doActivityLogResult();
    }

    @Test
    public void doSetMessage(){
        service.doSetMessage();
    }

}