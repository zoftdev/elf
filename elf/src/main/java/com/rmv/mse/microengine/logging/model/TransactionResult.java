package com.rmv.mse.microengine.logging.model;

/**
 * Created by zoftdev on 8/8/2017.
 */
public class TransactionResult {

    String tranCode;
    String tranDesc;
    String namespace="SBM";

    public TransactionResult() {
    }

    public TransactionResult(String tranCode, String tranDesc) {
        this.tranCode = tranCode;
        this.tranDesc = tranDesc;
    }

    public static final TransactionResult SUCCESS=new TransactionResult().setTranCode("0").setTranDesc("success");

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

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
