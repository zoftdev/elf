package com.rmv.mse.microengine.logging.logging.context;

import com.rmv.mse.microengine.logging.logging.exception.ActivityLoggingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zoftdev on 8/9/2017.
 */
@Service
public class LogContextService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public LogContextService() {
        logger.warn("LogContextService v1.3.1");
    }

    //Stack Of Transaction
    Map<Thread ,LinkedList<LogContext>> threadStackTransactionContext=new ConcurrentHashMap<>();
    Map<Thread ,LinkedList<LogActivityContext>> actStackTransactionContext =new ConcurrentHashMap<>();

    //store of child->parent
    Map<Thread,Thread> childParentMap=new ConcurrentHashMap<>();
    public LogContext addTransactionLoggingContext(LogContext logContext){

        LinkedList<LogContext> list = threadStackTransactionContext.get(Thread.currentThread());

        if(list==null){
            threadStackTransactionContext.put(Thread.currentThread(),new LinkedList<>());
        }else{
            //set parent tid
            logContext.setParentTransactionId(list.getLast().getTransactionId());
        }



        threadStackTransactionContext.get(Thread.currentThread()).add(logContext);
        return logContext;
    }

    public LogActivityContext addActivityLoggingContext(LogActivityContext activityContext){
        LinkedList<LogActivityContext> list = actStackTransactionContext.get(Thread.currentThread());
        if(list==null){
            list=new LinkedList<>();
            actStackTransactionContext.put(Thread.currentThread(),list);

        }
        list.add(activityContext);
        getCurrentContext().setLogActivityContext(activityContext);
        return activityContext;
    }

    //return prior activity context
    public LogActivityContext removeActivityLoggingContext(){
        assert(actStackTransactionContext.get(Thread.currentThread())!=null);
        LinkedList<LogActivityContext> list = actStackTransactionContext.get(Thread.currentThread());
        list.removeLast();
        //set current context's acitivity
        if(list.size()>0){
            getCurrentContext().setLogActivityContext(list.getLast());
            return list.getLast();
        } else{
            getCurrentContext().setLogActivityContext(null);
            return null;
        }


    }


    public LogContext remove(LogContext logContext){
        assert(threadStackTransactionContext.get(Thread.currentThread())!=null);

        LogContext context = threadStackTransactionContext.get(Thread.currentThread()).getLast();

        //cleanup child
        for(Thread childThead: context.getChildThread()){
            childThead.interrupt();
            childParentMap.remove(childThead);
        }
        LogContext removedLogContext = threadStackTransactionContext.get(Thread.currentThread()).removeLast();
        assert (removedLogContext==logContext);
        if( threadStackTransactionContext.get(Thread.currentThread()).size()==0){
            threadStackTransactionContext.remove(Thread.currentThread());
        }
        return removedLogContext;

    }

    public LogContext getCurrentContext() {
        if(threadStackTransactionContext.get(Thread.currentThread())!=null) {
            return threadStackTransactionContext.get(Thread.currentThread()).getLast();
        }else if(childParentMap.get(Thread.currentThread())!=null)
            return threadStackTransactionContext.get( childParentMap.get(Thread.currentThread()) ).getLast();
        else{
            throw new RuntimeException("no logging context");
        }
    }

    public LogContext joinContext(ContextSignature parent) {
        if(threadStackTransactionContext.get(parent.getThread())==null){
            throw new RuntimeException("Transaction not found");
        }
        LogContext context = threadStackTransactionContext.get(parent.getThread()).getLast();
        context.getChildThread().add(Thread.currentThread());
        childParentMap.put(Thread.currentThread(),parent.getThread());
        return context;
    }

    public ContextSignature getCurrentContextSignature() {
        assert(threadStackTransactionContext.get(Thread.currentThread())!=null);
        return new ContextSignature(Thread.currentThread());
    }

    public void waitChild() {

        waitChild(0);
    }

    public void waitChild(int max_wait_msec) {
        LogContext inFightContext = getCurrentContext();
        for (Thread t:inFightContext.getChildThread()){
            try {
                t.join(max_wait_msec);
            } catch (InterruptedException e) {
                throw new ActivityLoggingException("WaitChild fail:"+e.getMessage());
            }
        }
    }
}
