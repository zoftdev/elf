package com.rmv.mse.microengine.logging.model;

import java.util.*;

public class ActivitySlimContext {

    private String activityName;
    private Map<String, Object> keyValueMap;
    private List<Object> appendFields =null;
    private long begin;
    private Throwable throwable;

    public ActivitySlimContext(String activityName, Map<String,Object> keyValueMap, Object... appendFields) {
        init(activityName);
        this.keyValueMap = keyValueMap;
        this.appendFields.addAll(Arrays.asList(appendFields));
    }

    public ActivitySlimContext(String activityName) {
        init(activityName);
        this.keyValueMap=new HashMap<>();

    }

    private void init(String activityName) {
        this.begin=System.currentTimeMillis();
        this.activityName = activityName;
        this.appendFields =new ArrayList<>();
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Map<String, Object> getKeyValueMap() {
        return keyValueMap;
    }

    public Object putA(String key,Object value) {
        return keyValueMap.put(key,value);
    }



    public void replaceKeyValueMap(Map<String, Object> keyValueMap) {
        this.keyValueMap = keyValueMap;
    }

    public List<Object> getAppendFields() {
        return appendFields;
    }

    public boolean appendFieldsA(Object...e) {
        return appendFields.addAll(Arrays.asList(e) );
    }
    public void replaceAppendFieldsA(List<Object> putFields) {
        this.appendFields = putFields;
    }

    public long getBegin() {
        return begin;
    }

    public void changeBegin(long begin) {
        this.begin = begin;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
