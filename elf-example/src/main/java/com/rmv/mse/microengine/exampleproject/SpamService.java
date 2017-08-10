package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.logging.annotation.TransactionLogging;
import com.rmv.mse.microengine.logging.logging.context.TransactionLoggingContext;
import com.rmv.mse.microengine.logging.logging.context.TransactionLoggingContextFactory;
import com.rmv.mse.microengine.logging.logging.model.TransactionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.DatagramSocket;

/**
 * Created by zoftdev on 8/10/2017.
 */
@Service
public class SpamService {

    @Autowired
    TransactionLoggingContextFactory transactionLoggingContextFactory;

    @TransactionLogging
    public TransactionResult buyPackage(){
        TransactionLoggingContext context = transactionLoggingContextFactory.getInFightContext();
        context.getTransactionLogMap().put("msisdn","071111");
        context.getTransactionLogMap().put("tmp","a\nb\nc\thahaha\r\ntestja");
        smsSender.send();
        smsSender.test2();
        return  TransactionResult.SUCCESS;
    }

    @Autowired SMSSender smsSender;

    private void doMethod() {


    }


}
