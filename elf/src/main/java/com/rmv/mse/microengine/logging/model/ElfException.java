package com.rmv.mse.microengine.logging.model;

public class ElfException extends RuntimeException {

    String code;
    String desc;
    Throwable t;

    public ElfException(String code, String desc, Throwable t) {
        this.code = code;
        this.desc = desc;
        this.t = t;
    }

    public ElfException(String code, String desc) {
        this.code = code;
        this.desc = desc;
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
