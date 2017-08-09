package com.rmv.mse.microengine.logging.model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class MethodMetaData {
    Method method;
    int posOfTransactionLogging=-1;
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

    public int getPosOfTransactionLogging() {
        return posOfTransactionLogging;
    }

    public void setPosOfTransactionLogging(int posOfTransactionLogging) {
        this.posOfTransactionLogging = posOfTransactionLogging;
    }

    public boolean isLogResponse() {
        return isLogResponse;
    }

    public void setLogResponse(boolean logResponse) {
        isLogResponse = logResponse;
    }
}
