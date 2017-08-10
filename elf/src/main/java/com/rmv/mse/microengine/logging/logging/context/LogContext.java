package com.rmv.mse.microengine.logging.logging.context;

import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zoftdev on 8/7/2017.
 */
//todo revise method
public class LogContext {
    String transactionId;
    Map<String,Object> transactionLogMap;
    Map<String,Object> activityLogMap;
    Marker activityMarker;
    Marker transactionMarker;
    String parentTransactionId;

    //child
    Set<Thread> childThread=new ConcurrentHashMap().newKeySet();

    private LogContext() {
    }

    public LogContext(String transactionId, Map<String, Object> transactionLogMap, Map<String, Object> activityLogMap, Marker activityMarker, Marker transactionMarker) {
        this.transactionId = transactionId;
        this.transactionLogMap = transactionLogMap;
        this.activityLogMap = activityLogMap;
        this.activityMarker = activityMarker;
        this.transactionMarker = transactionMarker;
    }

    public static LogContext createBasic(){
        return new LogContext(
                UUID.randomUUID().toString(),
                new HashMap<>(),
                new HashMap<>(),
                Markers.appendFields(null),
                Markers.appendFields(null)
                );

    }

    public static LogContext createThreadSafe(){
        return new LogContext(
                UUID.randomUUID().toString(),
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>(),
                Markers.appendFields(null),
                Markers.appendFields(null)
        );

    }

    public static LogContext createEmpty() {
        return new LogContext();
    }



    public String getTransactionId() {
        return transactionId;
    }

    public LogContext setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    /**
     * Put to transaction level
     * @param key
     * @param v
     * @return
     */
    public Object putT(String key,Object v){
        return transactionLogMap.put(key,v);
    }

    /**
     * put to activity level
     * @param key
     * @param v
     * @return
     */
    public Object putA(String key,Object v){
        return activityLogMap.put(key,v);
    }


    public Map<String, Object> _getTransactionLogMap() {
        return transactionLogMap;
    }

    public void _setTransactionLogMap(Map<String, Object> transactionLogMap) {
        this.transactionLogMap = transactionLogMap;
    }

    public Map<String, Object> _getActivityLogMap() {
        return activityLogMap;
    }

    public void _setActivityLogMap(Map<String, Object> activityLogMap) {
        this.activityLogMap = activityLogMap;
    }



    //beware, marker not merge json element , lead to duplicate element
    public void appendFieldsA(Object o) {
        activityMarker.add(Markers.appendFields(o));
    }

    //beware, marker not merge json element , lead to duplicate element
    public void appendFieldsT(Object o) {
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

    //package access
    Set<Thread> getChildThread() {
        return childThread;
    }

    public void setChildThread(Set<Thread> childThread) {
        this.childThread = childThread;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    public void setParentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }


}
