package com.rmv.mse.microengine.logging.model;

public class ElfException extends RuntimeException {

    String code;
    String desc;
    Throwable t;

    public ElfException(String code, String desc, Throwable t) {
        super(code+":"+desc+":"+t.getMessage(),t);
        this.code = code;
        this.desc = desc;
        this.t = t;

    }

    public ElfException(String code, String desc) {

        super(code+":"+desc);
        this.code = code;
        this.desc = desc;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Throwable getT() {
        return t;
    }
}
