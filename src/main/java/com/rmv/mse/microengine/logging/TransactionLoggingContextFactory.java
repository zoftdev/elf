package com.rmv.mse.microengine.logging;

import com.rmv.mse.microengine.logging.model.TransactionLoggingContext;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by zoftdev on 8/9/2017.
 */
@Service
public class TransactionLoggingContextFactory {
    //Stack Of Transaction
    Map<Thread ,LinkedList<TransactionLoggingContext>> threadStackTransactionContext=new HashMap<>();

    public void addTransactionLoggingContext(TransactionLoggingContext transactionLoggingContext){

        LinkedList<TransactionLoggingContext> list = threadStackTransactionContext.get(Thread.currentThread());

        if(list==null){
            threadStackTransactionContext.put(Thread.currentThread(),new LinkedList<>());
        }
        threadStackTransactionContext.get(Thread.currentThread()).add(transactionLoggingContext);

    }

    public TransactionLoggingContext remove(TransactionLoggingContext transactionLoggingContext){
        assert(threadStackTransactionContext.get(Thread.currentThread())!=null);
        return threadStackTransactionContext.get(Thread.currentThread()).removeLast();

    }

    public TransactionLoggingContext getInFightContext() {
        assert(threadStackTransactionContext.get(Thread.currentThread())!=null);
        return threadStackTransactionContext.get(Thread.currentThread()).getLast();
    }
}
