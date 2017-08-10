package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.logging.context.ContextSignature;
import com.rmv.mse.microengine.logging.logging.LoggingKey;
import com.rmv.mse.microengine.logging.logging.annotation.ActivityLogging;
import com.rmv.mse.microengine.logging.logging.context.LogContextService;
import com.rmv.mse.microengine.logging.logging.annotation.TransactionLogging;
import com.rmv.mse.microengine.logging.logging.context.LogContext;
import com.rmv.mse.microengine.logging.logging.model.ActivityResult;
import com.rmv.mse.microengine.logging.logging.model.TransactionResult;
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




    @Autowired
    LogContextService logContextService;

    /**
     * This example show pattern of return TransactionResult
     */
    @TransactionLogging
    public TransactionResult example_hello_world() {

        TransactionResult ret = new TransactionResult();
        //do something
        return ret.setTranCode("0").setTranDesc("Success");
    }

    @TransactionLogging(logResponse = false)
    public String notLogResponse() {
        logContextService.getCurrentContext().appendFieldsT(TransactionResult.SUCCESS);
        //do something
        return "test";
    }

    @TransactionLogging(name = "newName")
    public TransactionResult overrideName() {

        TransactionResult ret = new TransactionResult();
        //do something
        return ret.setTranCode("0").setTranDesc("Success");
    }

    @TransactionLogging
    public TransactionResult log_id() {
        //Get id
        String transactionId = logContextService.getCurrentContext().getTransactionId();
        logger.debug("tid:{}",transactionId);

        TransactionResult ret = new TransactionResult();
        //do something
        return ret.setTranCode("0").setTranDesc("Success");
    }



    /**
     * This example show log msisdn recursively
     */
    @TransactionLogging
    public TransactionResult logging_and_pass_to_activity(
    ) {
        LogContext context = logContextService.getCurrentContext();
        context.putT(LoggingKey.MSISDN,"2222");

        exampleService.exampleLogging("hlex","java");
        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }

    @TransactionLogging
    public TransactionResult deepService(
    ) {
        LogContext context = logContextService.getCurrentContext();
        context.putT(LoggingKey.MSISDN,"2222");
        methodA();

        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }

    private void methodA() {
        methodB();
    }

    private void methodB() {
        exampleService.exampleLogging("hlex","java");
    }


    @TransactionLogging
    public TransactionResult doException(){
        throw new RuntimeException("test");
    }

    @TransactionLogging
    public TransactionResult doExceptionFromService(){
        exampleService.doException();
        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }

    @TransactionLogging
    public TransactionResult doThreadService(){

        ContextSignature contextSignature= logContextService.getCurrentContextSignature();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                logContextService.joinContext(contextSignature);
                exampleService.exampleLogging("hlex","pass");
            }
        };
        for (int i = 0; i < 10; i++) {

            Thread t = new Thread(runnable);
            t.start();
        }

        logContextService.waitChild(1000);
        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }


    /**
     * Example of Overlap Fiunction
     */


    @TransactionLogging
    public TransactionResult doOverlapFunction(){
        exampleService.foo();
        someClass.subFunction();
        return TransactionResult.SUCCESS;
    }

    @Autowired SomeClass someClass;

    @Service
    class SomeClass{

        @TransactionLogging
        public TransactionResult subFunction(){
            someActivity();
            return TransactionResult.SUCCESS;
        }

        @ActivityLogging
        public ActivityResult someActivity(){
            return ActivityResult.SUCCESS;
        }

    }

}
