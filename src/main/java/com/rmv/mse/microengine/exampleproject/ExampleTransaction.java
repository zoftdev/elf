package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.LoggingKey;
import com.rmv.mse.microengine.logging.annotation.InjectByLogging;
import com.rmv.mse.microengine.logging.annotation.TransactionLogging;
import com.rmv.mse.microengine.logging.model.TransactionLoggingContext;
import com.rmv.mse.microengine.logging.model.TransactionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zoftdev on 8/8/2017.
 */
@Service
public class ExampleTransaction {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    ExampleService exampleService;


    /**
     * This example show pattern of return TransactionResult
     */
    @TransactionLogging
    public TransactionResult example_hello_world() {

        TransactionResult ret = new TransactionResult().setTransactionId("1234");
        //do something
        return ret.setTranCode("0").setTranDesc("Success");
    }



    /**
     * This example show log msisdn recursively
     */
    @TransactionLogging
    public TransactionResult logging_and_pass_to_activity(
             @InjectByLogging TransactionLoggingContext transactionLoggingContext
    ) {
        transactionLoggingContext.getTransactionLogMap().put(LoggingKey.MSISDN,"2222");

        exampleService.exampleLogging("hlex","java");
        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }

    @TransactionLogging
    public TransactionResult deepService(
            @InjectByLogging TransactionLoggingContext transactionLoggingContext
    ) {
        transactionLoggingContext.getTransactionLogMap().put(LoggingKey.MSISDN,"2222");
        methodA();

        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }

    private void methodA() {
        methodB();
    }

    private void methodB() {
        exampleService.exampleLogging("hlex","java");
    }


}
