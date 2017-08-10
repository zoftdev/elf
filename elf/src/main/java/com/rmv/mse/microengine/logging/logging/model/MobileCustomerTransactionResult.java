package com.rmv.mse.microengine.logging.logging.model;

/**
 * Created by zoftdev on 8/8/2017.
 */
public class MobileCustomerTransactionResult extends  TransactionResult {

    String requestId;
    String messageId;
    String message;
    String provisioningId;
    String msisdn;



    public String getRequestId() {
        return requestId;
    }

    public MobileCustomerTransactionResult setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getMessageId() {
        return messageId;
    }

    public MobileCustomerTransactionResult setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MobileCustomerTransactionResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getProvisioningId() {
        return provisioningId;
    }

    public MobileCustomerTransactionResult setProvisioningId(String provisioningId) {
        this.provisioningId = provisioningId;
        return this;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public MobileCustomerTransactionResult setMsisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }
}
