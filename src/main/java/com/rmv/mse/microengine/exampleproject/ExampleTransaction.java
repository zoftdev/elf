package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.LoggingKey;
import com.rmv.mse.microengine.logging.annotation.LogMDC;
import com.rmv.mse.microengine.logging.annotation.LogParam;
import com.rmv.mse.microengine.logging.annotation.Transaction;
import com.rmv.mse.microengine.logging.model.TransactionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * Created by zoftdev on 8/8/2017.
 */
@Service
public class ExampleTransaction {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    ExampleService exampleService;

    @Transaction
    public String example_very_simple() {

        //do something

        return "ok";
    }


    /**
     * This example show pattern of return TransactionResult
     */
    @Transaction
    public TransactionResult example_hello_world() {
        TransactionResult ret = new TransactionResult().setTransactionId("1234");
        //do something
        return ret.setTranCode("0").setTranDesc("Success");
    }



    /**
     * This example show log to transaction
     */
    @Transaction
    public TransactionResult example_log_param(
            @LogParam(LoggingKey.MSISDN) String msisdn,
            @LogParam(LoggingKey.REQUEST_ID)String requestId
    ) {

        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }


    /**
     * This example show log msisdn recursively
     */
    @Transaction
    public TransactionResult example_log_param_recursive(
            @LogParam(value=LoggingKey.MSISDN,recursive=true) String msisdn,
            @LogParam(LoggingKey.REQUEST_ID)String requestId
    ) {

        exampleService.doActivity("hlex","java");
        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }




    /**
     * This example show log transactionId to transaction+activity
     */
    @Transaction
    public TransactionResult example_log_inside(String username , @LogMDC Map<String,Object> logMDC) {
        logMDC.put(LoggingKey.TRANSACTIONID, UUID.randomUUID().toString());

        //do something

        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }


    /**
     * This example show log msisdn recursively
     */
    @Transaction
    public TransactionResult withActivity() {
        exampleService.doActivity("hlex","java");
        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }

    /**
     * This example show log msisdn recursively
     */
    @Transaction
    public TransactionResult fullSet(
            @LogParam(value=LoggingKey.MSISDN,recursive=true) String msisdn,
            @LogParam(LoggingKey.REQUEST_ID)String requestId,
            @LogMDC Map<String,Object> logMDC
    ) {
        logMDC.put(LoggingKey.TRANSACTIONID, UUID.randomUUID().toString());

        exampleService.doActivity("hlex","java");
        exampleService.doBasic("hlex" );

        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }



}
