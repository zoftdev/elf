package com.rmv.mse.microengine.logging.logging.model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class MethodMetaData {
    Method method;

    boolean isOverrideName=false;
    String overrideName;
    boolean isLogResponse=true;

    public MethodMetaData(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }


    public boolean isLogResponse() {
        return isLogResponse;
    }

    public void setLogResponse(boolean logResponse) {
        isLogResponse = logResponse;
    }

    public String getOverrideName() {
        return overrideName;
    }

    public void setOverrideName(String overrideName) {
        isOverrideName=true;
        this.overrideName = overrideName;
    }

    public boolean isOverrideName() {
        return isOverrideName;
    }
}
