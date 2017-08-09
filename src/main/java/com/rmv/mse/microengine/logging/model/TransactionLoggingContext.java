package com.rmv.mse.microengine.logging.model;

import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zoftdev on 8/7/2017.
 */

public class TransactionLoggingContext {
    String transactionId;
    Map<String,Object> transactionLogMap;
    Map<String,Object> activityLogMap;
    Marker activityMarker;
    Marker transactionMarker;

    private TransactionLoggingContext() {
    }

    public TransactionLoggingContext(String transactionId, Map<String, Object> transactionLogMap, Map<String, Object> activityLogMap, Marker activityMarker, Marker transactionMarker) {
        this.transactionId = transactionId;
        this.transactionLogMap = transactionLogMap;
        this.activityLogMap = activityLogMap;
        this.activityMarker = activityMarker;
        this.transactionMarker = transactionMarker;
    }

    public static  TransactionLoggingContext getDummy(){
        return new TransactionLoggingContext(
                UUID.randomUUID().toString(),
                new HashMap<>(),
                new HashMap<>(),
                Markers.appendFields(null),
                Markers.appendFields(null)
                );

    }

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionLoggingContext setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Map<String, Object> getTransactionLogMap() {
        return transactionLogMap;
    }

    public void setTransactionLogMap(Map<String, Object> transactionLogMap) {
        this.transactionLogMap = transactionLogMap;
    }

    public Map<String, Object> getActivityLogMap() {
        return activityLogMap;
    }

    public void setActivityLogMap(Map<String, Object> activityLogMap) {
        this.activityLogMap = activityLogMap;
    }



    //beware, marker not merge json element , lead to duplicate element
    public void appendActivityFields(Object o) {
        activityMarker.add(Markers.appendFields(o));
    }

    //beware, marker not merge json element , lead to duplicate element
    public void appendTransactionFields(Object o) {
        transactionMarker.add(Markers.appendFields(o));
    }

    public Marker getActivityMarker() {
        return activityMarker;
    }

    public void setActivityMarker(Marker activityMarker) {
        this.activityMarker = activityMarker;
    }

    public Marker getTransactionMarker() {
        return transactionMarker;
    }

    public void setTransactionMarker(Marker transactionMarker) {
        this.transactionMarker = transactionMarker;
    }
}
