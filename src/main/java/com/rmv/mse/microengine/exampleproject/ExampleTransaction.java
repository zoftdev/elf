package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.LoggingKey;
import com.rmv.mse.microengine.logging.annotation.LogMDC;
import com.rmv.mse.microengine.logging.annotation.LogParam;
import com.rmv.mse.microengine.logging.annotation.Transaction;
import com.rmv.mse.microengine.logging.annotation.TransactionMarker;
import com.rmv.mse.microengine.logging.model.TransactionResult;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
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
     * This example show log transactionId to transaction+activity
     */
    @Transaction
    public TransactionResult example_log_inside(String username , @TransactionMarker Marker marker) {

        MDC.put(LoggingKey.TRANSACTIONID, UUID.randomUUID().toString());
        marker.add(Markers.append(LoggingKey.MSISDN,"2222"));

        //do something

        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }


    /**
     * This example show log msisdn recursively
     */
    @Transaction
    public TransactionResult withActivity() {
        exampleService.exampleLogging("hlex","java",null,null);
        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }

    /**
     * This example show log msisdn recursively
     */
    @Transaction
    public TransactionResult fullSet(
            @LogParam(value=LoggingKey.MSISDN) String msisdn,
            @LogParam(LoggingKey.REQUEST_ID)String requestId,
            @TransactionMarker Marker marker

    ) {
        MDC.put(LoggingKey.TRANSACTIONID, UUID.randomUUID().toString());
        marker.add(Markers.append(LoggingKey.MSISDN,"2222"));
        exampleService.exampleLogging("hlex","java",null,null);
        return new TransactionResult().setTranCode("0").setTranDesc("Success");
    }



}
