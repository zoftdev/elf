package com.rmv.mse.microengine.logging.context;

import com.rmv.mse.microengine.logging.model.ActivitySlimContext;
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
    Map<String, Object> transactionLogMap;
    Map<String, Object> transactionOnlyLogMap;
    Marker transactionMarker;
    //not show in activity
    Marker transactionOnlyMarker;
    String parentTransactionId;
    String functionId;



    ActivitySlimContext currentActivitySlimContext;

    //child
    Set<Thread> childThread = new ConcurrentHashMap().newKeySet();
    private LogActivityContext logActivityContext;
    private String overrideActivityName;
    private long overrideBegin;

    private LogContext() {

    }

    public LogContext(String transactionId, Map<String, Object> transactionLogMap, Map<String, Object> transactionOnlyLogMap) {
        this.transactionId = transactionId;
        this.transactionLogMap = transactionLogMap;
        this.transactionOnlyLogMap=transactionOnlyLogMap;
        transactionMarker = Markers.appendFields(null);
        transactionOnlyMarker = Markers.appendFields(null);
    }

    public static LogContext createBasic() {
        return new LogContext(
                UUID.randomUUID().toString(),
                new ConcurrentHashMap<>(),
                new ConcurrentHashMap<>()


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
     *
     * @param key
     * @param v
     * @return
     */
    public Object putT(String key, Object v) {
        return transactionLogMap.put(key, v);
    }
    public Object putOnlyT(String key, Object v) {
        return transactionOnlyLogMap.put(key, v);
    }


    /**
     * put to activity level
     *
     * @param key
     * @param v
     * @return
     */
    public Object putA(String key, Object v) {
        return logActivityContext.getActivityLogMap().put(key, v);
    }


    public Map<String, Object> _getTransactionLogMap() {
        return transactionLogMap;
    }

    public void _setTransactionLogMap(Map<String, Object> transactionLogMap) {
        this.transactionLogMap = transactionLogMap;
    }

    public Map<String, Object> _getTransactionOnlyLogMap() {
        return transactionOnlyLogMap;
    }

    public void _setTransactionOnlyLogMap(Map<String, Object> transactionOnlyLogMap) {
        this.transactionOnlyLogMap = transactionOnlyLogMap;
    }

    //beware, marker not merge json element , lead to duplicate element
    public void appendFieldsA(Object o) {
        getLogActivityContext().getActivityMarker().add(Markers.appendFields(o));
    }

    //beware, marker not merge json element , lead to duplicate element
    public void appendFieldsT(Object o) {
        transactionMarker.add(Markers.appendFields(o));
    }

    //beware, marker not merge json element , lead to duplicate element

    public void appendFieldsOnlyT(Object o) {
        transactionOnlyMarker.add(Markers.appendFields(o));
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

    void setChildThread(Set<Thread> childThread) {
        this.childThread = childThread;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    void setParentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }


    public void setLogActivityContext(LogActivityContext logActivityContext) {
        this.logActivityContext = logActivityContext;
    }

    public LogActivityContext getLogActivityContext() {
        return logActivityContext;
    }


    public Marker getTransactionOnlyMarker() {
        return transactionOnlyMarker;
    }

    public void setOverrideActivityName(String overrideActivityName) {
        this.overrideActivityName = overrideActivityName;
    }

    public String getOverrideActivityName() {
        return overrideActivityName;
    }

    public void setOverrideBegin(long overrideBegin) {
        this.overrideBegin = overrideBegin;
    }

    public long getOverrideBegin() {
        return overrideBegin;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }


    public ActivitySlimContext getCurrentActivitySlimContext() {
        return currentActivitySlimContext;
    }

    public void setCurrentActivitySlimContext(ActivitySlimContext currentActivitySlimContext) {
        this.currentActivitySlimContext = currentActivitySlimContext;
    }

}
