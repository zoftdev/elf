package com.rmv.mse.microengine.logging.logging.context;

/**
 * Created by zoftdev on 8/10/2017.
 */
public class ContextSignature {
    private Thread thread;

    public ContextSignature(Thread thread) {
        this.thread=thread;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
