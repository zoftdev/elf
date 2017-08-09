package com.rmv.mse.microengine.logging.model;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class ActivityLoggingMessage  {
    long begin;

    long processTime;
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


    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }



}
