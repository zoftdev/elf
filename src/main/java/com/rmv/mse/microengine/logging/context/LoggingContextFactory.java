package com.rmv.mse.microengine.logging.context;
import com.rmv.mse.microengine.logging.context.TransactionLoggingContext;

import java.util.UUID;

/**
 * Created by zoftdev on 8/8/2017.
 */
public class LoggingContextFactory {

    public static LoggingContext buildBasic(){
        LoggingContext loggingContext=new LoggingContext();
        loggingContext.setTransactionContext( new TransactionLoggingContext().setTransactionId(UUID.randomUUID().toString()));
        return loggingContext;
    }
}
