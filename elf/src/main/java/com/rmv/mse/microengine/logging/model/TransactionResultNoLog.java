package com.rmv.mse.microengine.logging.model;

public class TransactionResultNoLog extends TransactionResult implements NoLogInterface {
    public TransactionResultNoLog() {
    }

    public TransactionResultNoLog(String tranCode, String tranDesc) {
        super(tranCode, tranDesc);
    }
}
