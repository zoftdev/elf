package com.rmv.mse.microengine.logging.logging;

import com.rmv.mse.microengine.logging.logging.annotation.ActivityLogging;
import com.rmv.mse.microengine.logging.logging.annotation.TransactionLogging;
import com.rmv.mse.microengine.logging.logging.context.TransactionLoggingContext;
import com.rmv.mse.microengine.logging.logging.exception.ActivityLoggingException;
import com.rmv.mse.microengine.logging.logging.model.ClassMetaData;
import com.rmv.mse.microengine.logging.logging.model.MethodMetaData;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zoftdev on 8/7/2017.
 */
public class ClassMetaDataCache {

    private Map<Class,ActivityLoggingException> blackListClass =new HashMap();
    private Map<Class,ClassMetaData> cachedClass=new HashMap<>();

    /**
     * Cache class verification
     * - method with @ActivityLogging must not overloaded
     * - V8.8 support TransactionLoggingContext param
     * @param c
     * @return
     */
    public boolean initialize(Class c){

        // cached blacklist
        if(blackListClass.containsKey(c)){
            throw blackListClass.get(c);
        }

        //cached whilelist
        if(cachedClass.containsKey(c)){
            return true;
        }

        boolean overloaded = isOverloadActivityAnnotation(c.getMethods());
        if(overloaded){
            ActivityLoggingException exception = new ActivityLoggingException("Overload @Activty metod is not support");
            blackListClass.put(c,exception);
            throw exception;
        }else{
            ClassMetaData classMetaData =new ClassMetaData();
            for (Method method : c.getMethods())
            {
                //only method with @activity
                if(method.getAnnotation(ActivityLogging.class)!=null) {
                    ActivityLogging annotation = method.getAnnotation(ActivityLogging.class);
                    MethodMetaData methodMetaData = new MethodMetaData(method);
                    classMetaData.getActivtyMethod().put(method.getName(), methodMetaData);
                    methodMetaData.setLogResponse( annotation.logResponse());
                    if(!annotation.name().equals("")){
                        methodMetaData.setOverrideName(annotation.name());
                    }
                }
                //only method with @transactionLogging
                if(method.getAnnotation(TransactionLogging.class)!=null) {
                    TransactionLogging annotation = method.getAnnotation(TransactionLogging.class);
                    MethodMetaData methodMetaData = new MethodMetaData(method);
                    classMetaData.getTransactionMethod().put(method.getName(), methodMetaData);
                    methodMetaData.setLogResponse( annotation.logResponse());
                    if(!annotation.name().equals("")){
                        methodMetaData.setOverrideName(annotation.name());
                    }
                }

            }
            cachedClass.put(c, classMetaData);
        }
        return true;

    }




    /**
     * Overload method is not supported
     */
    public boolean isOverloadActivityAnnotation(final Method[] methods)
    {
        Set<String> pool = new HashSet<String>();
        for (Method method : methods)
        {
            //only method with @activity
            if(method.getAnnotation(ActivityLogging.class)==null) continue;
            if (pool.contains(method.getName())) return true;
            pool.add(method.getName());
        }
        return false;
    }

    public Method getActivtyMethod(Class c, String methodName) {
        if(!cachedClass.containsKey(c)) throw new ActivityLoggingException("Need call initiailzed first");
        MethodMetaData methodMetaData = cachedClass.get(c).getActivtyMethod().get(methodName);
        assert (methodMetaData!=null);
        Method method =methodMetaData .getMethod();

        return method;

    }


    public Map<Class, ClassMetaData> getCachedClass() {
        return cachedClass;
    }
}
