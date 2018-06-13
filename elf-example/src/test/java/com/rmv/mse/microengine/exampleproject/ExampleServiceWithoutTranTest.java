package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.context.LogContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ExampleServiceWithoutTranTest {

    @Before
    public void init(){
//        logContextService.addTransactionLoggingContext(LogContext.createBasic());
    }

    @Autowired
    ExampleService service;
    @Test//(expected = RuntimeException.class)
    public void doActivityLogResult() throws Exception {
        try {
            service.doActivityLogResult();
            fail("should exception");
        }catch (Throwable t){

            assertThat(t.getMessage(),is("no logging context"));

        }


    }
}
