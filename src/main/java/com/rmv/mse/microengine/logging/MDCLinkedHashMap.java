package com.rmv.mse.microengine.logging;

import com.rmv.mse.microengine.logging.model.TransactionLoggingContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zoftdev on 8/8/2017.
 */
public class MDCLinkedHashMap extends LinkedHashMap<Thread,TransactionLoggingContext> {

    public MDCLinkedHashMap() {
        super(1000);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Thread, TransactionLoggingContext> eldest) {
        return size() > 1000;
    }
}
