package com.rmv.mse.microengine.logging.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zoftdev on 8/7/2017.
 * Any method that return this class will be logged.
 */
public class ActivityResult {
    @JsonProperty
    String code;
    @JsonProperty
    String namespace;
    @JsonProperty
    String description;


    public ActivityResult(String code, String namespace, String description) {
        this.code = code;
        this.namespace = namespace;
        this.description = description;
    }

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
}
