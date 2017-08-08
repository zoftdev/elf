package com.rmv.mse.microengine.logging.context;

/**
 * Created by zoftdev on 8/8/2017.
 */
public class LoggingContext {
    TransactionLoggingContext transactionContext;
    ServiceLoggingContext  sessionLoggingContext;

    public TransactionLoggingContext getTransactionContext() {
        return transactionContext;
    }

    public void setTransactionContext(TransactionLoggingContext transactionContext) {
        this.transactionContext = transactionContext;
    }

    public ServiceLoggingContext getSessionLoggingContext() {
        return sessionLoggingContext;
    }

    public void setSessionLoggingContext(ServiceLoggingContext sessionLoggingContext) {
        this.sessionLoggingContext = sessionLoggingContext;
    }
}
