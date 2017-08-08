package com.rmv.mse.microengine.logging;

import com.rmv.mse.microengine.logging.annotation.Activity;
import com.rmv.mse.microengine.logging.annotation.LogMDC;
import com.rmv.mse.microengine.logging.annotation.LogParam;
import com.rmv.mse.microengine.logging.exception.ActivityLoggingException;

import java.lang.annotation.Annotation;
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
     * - method with @Activity must not overloaded
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
                if(method.getAnnotation(Activity.class)!=null) {
                    MethodMetaData methodMetaData = new MethodMetaData(method);
                    classMetaData.getActivtyMethod().put(method.getName(), methodMetaData);
                    //find @logParam
                    methodMetaData.setPositionOfLogParam( mapLogParam(method));
                    //find TransactionLoggingContext
                    methodMetaData.setPosOfLogMDC(findLogMDCPosition(method));

                }

            }
            cachedClass.put(c, classMetaData);
        }
        return true;

    }

    public int findLogMDCPosition(Method method) {
        int i=0;
        for(Annotation[] annotations:method.getParameterAnnotations()){
            //TODO : support subclass
            for(Annotation a:annotations) {
                if (a instanceof LogMDC) {
                    return i;
                }

            }
            i++;
        }
        return -1;
    }

    /**
     * Map position to paramName
     * @param method
     * @return
     */
    public Map<Integer, String> mapLogParam(Method method) {
        Map<Integer,String> positionOfLogParam=new HashMap<>();
        int pos=0;
        for ( Annotation[] annotations:method.getParameterAnnotations()){
            for (Annotation annotation : annotations) {
                if (annotation instanceof LogParam) {
                    LogParam logParamAnnotation = (LogParam) annotation;
                    String paramName=logParamAnnotation.value();
                    positionOfLogParam.put(pos,paramName);
                }
            }
            pos++;
        }
        return positionOfLogParam;
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
            if(method.getAnnotation(Activity.class)==null) continue;
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

    public Map<String, Object> getAnnotatedParameterValue(Class c,String methodName, Object[] args) {
        Map<String, Object> ret=new HashMap<>();
        assert (cachedClass.containsKey(c));
        Map<Integer,String> posToKey=cachedClass.get(c).getActivtyMethod().get(methodName).getPositionOfLogParam();
        for (Integer pos : posToKey.keySet()) {

            ret.put(posToKey.get(pos),args[pos]);

        }
        return ret;
    }

    public Map<Class, ClassMetaData> getCachedClass() {
        return cachedClass;
    }
}
