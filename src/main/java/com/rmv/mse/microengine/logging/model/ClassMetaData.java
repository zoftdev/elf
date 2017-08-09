package com.rmv.mse.microengine.logging.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class ClassMetaData {
    //meethodName->Method
    Map<String,MethodMetaData> activtyMethod=new HashMap<>();

    public Map<String, MethodMetaData> getActivtyMethod() {
        return activtyMethod;
    }

    public void setActivtyMethod(Map<String, MethodMetaData> activtyMethod) {
        this.activtyMethod = activtyMethod;
    }
}
