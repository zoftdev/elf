package com.rmv.mse.microengine.logging.exception;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class ActivityLoggingException  extends  RuntimeException{
    public ActivityLoggingException(String s) {
        super(s);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
