package com.rmv.mse.microengine.logging.context;

import com.rmv.mse.microengine.logging.exception.ActivityLoggingException;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zoftdev on 8/9/2017.
 */
@Service
public class TransactionLoggingContextFactory {
    //Stack Of Transaction
    Map<Thread ,LinkedList<TransactionLoggingContext>> threadStackTransactionContext=new ConcurrentHashMap<>();

    //store of child->parent
    Map<Thread,Thread> childParentMap=new ConcurrentHashMap<>();
    public TransactionLoggingContext addTransactionLoggingContext(TransactionLoggingContext transactionLoggingContext){

        LinkedList<TransactionLoggingContext> list = threadStackTransactionContext.get(Thread.currentThread());

        if(list==null){
            list=threadStackTransactionContext.put(Thread.currentThread(),new LinkedList<>());
        }else{
            //set parent tid
            transactionLoggingContext.setParentTransactionId(list.getLast().getTransactionId());
        }



        threadStackTransactionContext.get(Thread.currentThread()).add(transactionLoggingContext);
        return transactionLoggingContext;
    }

    public TransactionLoggingContext remove(TransactionLoggingContext transactionLoggingContext){
        assert(threadStackTransactionContext.get(Thread.currentThread())!=null);

        TransactionLoggingContext context = threadStackTransactionContext.get(Thread.currentThread()).getLast();

        //cleanup child
        for(Thread childThead: context.getChildThread()){
            childThead.interrupt();
            childParentMap.remove(childThead);
        }
        return threadStackTransactionContext.get(Thread.currentThread()).removeLast();

    }

    public TransactionLoggingContext getInFightContext() {
        if(threadStackTransactionContext.get(Thread.currentThread())!=null) {
            return threadStackTransactionContext.get(Thread.currentThread()).getLast();
        }else if(childParentMap.get(Thread.currentThread())!=null)
            return threadStackTransactionContext.get( childParentMap.get(Thread.currentThread()) ).getLast();
        else{
            throw new RuntimeException("no logging context");
        }
    }

    public void joinContext(ContextSignature parent) {
        if(threadStackTransactionContext.get(parent.getThread())==null){
            throw new RuntimeException("Transaction not found");
        }
        TransactionLoggingContext context = threadStackTransactionContext.get(parent.getThread()).getLast();
        context.getChildThread().add(Thread.currentThread());
        childParentMap.put(Thread.currentThread(),parent.getThread());
    }

    public ContextSignature getInFightContextSignature() {
        assert(threadStackTransactionContext.get(Thread.currentThread())!=null);
        return new ContextSignature(Thread.currentThread());
    }

    public void waitChild() {

        waitChild(0);
    }

    public void waitChild(int max_wait_msec) {
        TransactionLoggingContext inFightContext = getInFightContext();
        for (Thread t:inFightContext.getChildThread()){
            try {
                t.join(max_wait_msec);
            } catch (InterruptedException e) {
                throw new ActivityLoggingException("WaitChild fail:"+e.getMessage());
            }
        }
    }
}
