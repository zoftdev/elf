package com.rmv.mse.microengine.logging;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class MethodMetaData {
    Method method;
    //map postition of param to param name;
    Map<Integer,String> positionOfLogParam=new HashMap<>();
    int posOfLogMDC =-1;

    public MethodMetaData(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<Integer, String> getPositionOfLogParam() {
        return positionOfLogParam;
    }

    public void setPositionOfLogParam(Map<Integer, String> positionOfLogParam) {
        this.positionOfLogParam = positionOfLogParam;
    }

    public int getPosOfLogMDC() {
        return posOfLogMDC;
    }

    public void setPosOfLogMDC(int posOfLogMDC) {
        this.posOfLogMDC = posOfLogMDC;
    }
}
