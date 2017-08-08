package com.rmv.mse.microengine.logging;

import java.util.HashMap;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class ActivityLoggingMessage  {
    long begin;

    long diff;
    Object request;
    Object response;
    String activity;


    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }


    public long getDiff() {
        return diff;
    }

    public void setDiff(long diff) {
        this.diff = diff;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
