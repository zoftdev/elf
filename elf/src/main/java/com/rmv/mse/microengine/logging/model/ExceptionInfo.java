package com.rmv.mse.microengine.logging.model;

import java.util.Arrays;

public class ExceptionInfo {
    public String exceptionMessage;
    public String exceptionClass;
    public boolean exception=true;
    public String exceptionStackTrace;

    public ExceptionInfo(String exceptionMessage, String exceptionClass, String exceptionStackTrace) {
        this.exceptionMessage = exceptionMessage;
        this.exceptionClass = exceptionClass;
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public ExceptionInfo(Throwable t) {
        this.exceptionClass=t.getClass().getSimpleName();
        this.exceptionMessage=t.getMessage();
        this.exceptionStackTrace= Arrays.toString(t.getStackTrace());
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    public void setExceptionStackTrace(String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }
}
