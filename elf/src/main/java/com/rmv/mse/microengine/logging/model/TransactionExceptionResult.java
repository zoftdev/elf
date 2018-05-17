package com.rmv.mse.microengine.logging.model;

import java.util.Arrays;

public class TransactionExceptionResult extends  TransactionResult {
    private String exceptionClass;
    private String exceptionMessage;
    private String exceptionStackTrace;

    public TransactionExceptionResult(String tranCode, String tranDesc, Throwable t  ) {
        super(tranCode, tranDesc);
        this.exceptionClass=t.getClass().getSimpleName();
        this.exceptionStackTrace= Arrays.toString(t.getStackTrace());
        this.exceptionMessage=t.getMessage();
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }
}
