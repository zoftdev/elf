package com.rmv.mse.microengine.logging.logging.model;

/**
 * Created by zoftdev on 8/7/2017.
 * Any method that return this class will be logged.
 */
public class ActivityResult {
    String code;
    String namespace;
    String description;

    public ActivityResult() {
    }

    public ActivityResult(String code, String namespace, String description) {
        this.code = code;
        this.namespace = namespace;
        this.description = description;
    }

    public static final ActivityResult SUCCESS= new ActivityResult("0","SBM","Success");;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return    code + '|' +
                 namespace + '|' +
                 description ;

    }
}
