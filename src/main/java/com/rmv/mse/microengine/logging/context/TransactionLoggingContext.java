package com.rmv.mse.microengine.logging.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zoftdev on 8/7/2017.
 */

public class TransactionLoggingContext {
    String transactionId= UUID.randomUUID().toString();
    Map<String,Object> map=new HashMap<>();

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionLoggingContext setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
