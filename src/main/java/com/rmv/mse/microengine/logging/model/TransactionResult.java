package com.rmv.mse.microengine.logging.model;

/**
 * Created by zoftdev on 8/8/2017.
 */
public class TransactionResult {
    String transactionId; //Automatic fill data
    String tranCode;
    String tranDesc;

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionResult setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getTranCode() {
        return tranCode;
    }

    public TransactionResult setTranCode(String tranCode) {
        this.tranCode = tranCode;
        return this;
    }

    public String getTranDesc() {
        return tranDesc;
    }

    public TransactionResult setTranDesc(String tranDesc) {
        this.tranDesc = tranDesc;
        return this;
    }

}
