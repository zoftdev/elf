package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.logging.LoggingKey;
import com.rmv.mse.microengine.logging.logging.annotation.ActivityLogging;
import com.rmv.mse.microengine.logging.logging.context.TransactionLoggingContext;
import com.rmv.mse.microengine.logging.logging.context.TransactionLoggingContextFactory;
import com.rmv.mse.microengine.logging.logging.model.ActivityResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zoftdev on 8/10/2017.
 */
@Service
public class SMSSender {

    @Autowired
    TransactionLoggingContextFactory transactionLoggingContextFactory;

    @ActivityLogging(name="SendSMS",logResponse=false)
    public ActivityResult send() {
        TransactionLoggingContext context = transactionLoggingContextFactory.getInFightContext();
        context.getTransactionLogMap().put(LoggingKey.IMSI,"52000");
        context.getActivityLogMap().put("messageId","12222");
        return  ActivityResult.SUCCESS;
    }

    @ActivityLogging
    public ActivityResult test2(){
        return  ActivityResult.SUCCESS;
    }
}
