/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rmv.mse.microengine.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmv.mse.microengine.ELFConst;
import com.rmv.mse.microengine.logging.context.LogActivityContext;
import com.rmv.mse.microengine.logging.context.LogContextService;
import com.rmv.mse.microengine.logging.model.*;
import com.rmv.mse.microengine.logging.context.LogContext;
import com.rmv.mse.microengine.logging.prop.Error;
import com.rmv.mse.microengine.logging.prop.LoggingKey;
import net.logstash.logback.marker.Markers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingService {

    ObjectMapper objectMapper=new ObjectMapper();

	@Value("microengine.logging.servicename")
	String servicename;


	@Autowired
    LogContextService logContextService;

	String host;

    public LoggingService() {
        host=getHostName();
    }

    protected String getHostName() {
        if(System.getenv(ELFConst.HOSTNAME_ENV)!=null){
            return System.getenv(ELFConst.HOSTNAME_ENV);
        }
        return networkUtil.getHostByName();
    }

    ClassMetaDataCache classMetaDataCache =new ClassMetaDataCache();

	private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Logger loggerStash = LoggerFactory.getLogger("stash");
    private final Logger loggerStashActivity = LoggerFactory.getLogger("stash-activity");
    private NetworkUtil networkUtil=new NetworkUtil();

    @Around("@annotation(com.rmv.mse.microengine.logging.annotation.TransactionLog)")
    public Object transactionLogging(ProceedingJoinPoint pjp)throws Throwable{

        //func name
        String methodName=pjp.getSignature().getName();
        Class c=pjp.getSignature().getDeclaringType();
        classMetaDataCache.initialize(c);
        ClassMetaData classMetaData = classMetaDataCache.getCachedClass().get(c);
        MethodMetaData methodMetaData = classMetaData.getTransactionMethod().get(methodName);

        //create context
        LogContext context = logContextService.addTransactionLoggingContext(LogContext.createBasic());
        try {
            Marker marker = context.getTransactionMarker();
            Throwable t = null;
            Object ret = null;
            long begin = System.currentTimeMillis();
//            logger.trace("{} begin {}",context.getTransactionId(),System.currentTimeMillis());
            //process name
            String processName = methodName;
            if (methodMetaData.isOverrideName()) {
                processName = methodMetaData.getOverrideName();
            }
            context.setFunctionId(processName);
            marker.add(Markers.append(LoggingKey.PROCESS, processName));

            try {
                ret = pjp.proceed();
                if(ret instanceof NoLogInterface) return ret;
                if (methodMetaData.isLogResponse())
                    if (ret instanceof Map) {
                        marker.add(Markers.appendEntries((Map<?, ?>) ret));
                    } else
                        marker.add(Markers.appendFields(ret));
            } catch (Throwable throwable) {
                logger.error("Error in Process {}", throwable.getMessage(), throwable);
                t = throwable;
                marker.add(Markers.appendFields(appendExceptionField(t)));

            }
            long endl=System.currentTimeMillis();
//            logger.trace("{} end {} ",context.getTransactionId(),endl);
            long processTime = endl - begin;

            //add Transaction Only marker
            marker.add(context.getTransactionOnlyMarker());

            //type
            marker.add(Markers.append(LoggingKey.TYPE, LoggingKey.TRANSACTION));



            //time , begin
            marker.add(Markers.append(LoggingKey.PROCESS_TIME, processTime));
            marker.add(Markers.append(LoggingKey.BEGIN, begin));

            //id
            marker.add(Markers.append(LoggingKey.TRANSACTIONID, context.getTransactionId()));

            //map
            marker.add(Markers.appendEntries(context._getTransactionLogMap()));
            marker.add(Markers.appendEntries(context._getTransactionOnlyLogMap()));

            //parent
            if (context.getParentTransactionId() != null) {
                marker.add(Markers.append(LoggingKey.PARTENT_TRANSACTION_ID, context.getParentTransactionId()));
            }

            //host
            marker.add(Markers.append(LoggingKey.HOSTNAME, host));

            //version
            marker.add(Markers.append(LoggingKey.ELFVERSION_KEY, LoggingKey.ELFVERSION_VALUE));


            loggerStash.info(marker, "Function {} processed for {} ms", processName, processTime);


            //result
            if (t != null) {
                throw t;
            } else {
                return ret;
            }
        } finally {
            logContextService.remove(context);
        }
    }

    private Object appendExceptionField(Throwable t) {
        if(t instanceof  ElfException){
            ElfException e = (ElfException) t;
            return new TransactionExceptionResult(e.getCode(),e.getDesc(),t);
        }else
            return  new TransactionExceptionResult(Error.E77000,   "Exception:" + t.getMessage(),t);
    }

    //todo overlap service
	@Around("@annotation(com.rmv.mse.microengine.logging.annotation.ActivityLog)")
	public Object activityLogging(ProceedingJoinPoint pjp) throws Throwable {
		// start stopwatch
        Throwable t=null;
        ActivityResult throwActivityResult=null;
		Object retVal = null;
        String methodName=pjp.getSignature().getName();
        Class c=pjp.getSignature().getDeclaringType();
        classMetaDataCache.initialize(c);
        MethodMetaData methodMetaData = classMetaDataCache.getCachedClass().get(c).getActivtyMethod().get(methodName);
        Method method = methodMetaData.getMethod();
        LogContext context= logContextService.getCurrentContext();
        LogActivityContext logActivityContext = logContextService.addActivityLoggingContext(LogActivityContext.createBasic());
        try {
            Marker activityMarker = logActivityContext.getActivityMarker();

            //        findTransactionLoggingParam(pjp);

            long begin = System.currentTimeMillis();
            try {

                retVal = pjp.proceed();
                if(retVal instanceof NoLogInterface) return retVal;
                //no doException
            }catch (ElfException elfExcepion){
                elfExcepion.printStackTrace();
                throwActivityResult = new ActivityResult(elfExcepion.getCode(), "SBM", elfExcepion.getDesc());
                ExceptionInfo exceptionInfo=new ExceptionInfo(elfExcepion);
//                context.appendFieldsOnlyT(exceptionInfo);
                context.appendFieldsA(exceptionInfo);
                t = elfExcepion;

            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throwActivityResult = new ActivityResult(Error.E77000, "SBM", "Exception:"+throwable.getClass().getSimpleName()+"Message:" + throwable.getMessage());
                ExceptionInfo exceptionInfo=new ExceptionInfo(throwable);
                context.appendFieldsOnlyT(exceptionInfo);
                context.appendFieldsA(exceptionInfo);
                t = throwable;
                //		throw throwable;
            }
            // stop stopwatch
            long end = System.currentTimeMillis();

            //support orverride begin
            if(context.getOverrideBegin()!=0){
                begin=context.getOverrideBegin();
            }
            long diff = end - begin;


            String activityName = methodName;
            if(context.getOverrideActivityName()!=null){
                activityName=context.getOverrideActivityName();
            }
            else if (methodMetaData.isOverrideName()) {
                activityName = methodMetaData.getOverrideName();
            }


            //apply map to marker with priority: retval union marker  union( a map-> t map)
            Map<String, Object> mapToLog = new HashMap<>();
            mapToLog.putAll(context._getTransactionLogMap());
            mapToLog.putAll(context.getLogActivityContext().getActivityLogMap());
            activityMarker.add(Markers.appendEntries(mapToLog));
            activityMarker.add(context.getTransactionMarker());


            activityMarker.add(Markers.append(LoggingKey.TYPE, LoggingKey.ACTIVITY));
            //id
            activityMarker.add(Markers.append(LoggingKey.TRANSACTIONID, context.getTransactionId()));

            activityMarker.add(Markers.append(LoggingKey.ACTIVITY_ID, logActivityContext.getActivityId()));

            activityMarker.add(Markers.append(LoggingKey.PROCESS, context.getFunctionId()));
            activityMarker.add(Markers.append(LoggingKey.PROCESS_TIME, diff));
            activityMarker.add(Markers.append(LoggingKey.BEGIN, begin));
            activityMarker.add(Markers.append(LoggingKey.ACTIVITY, activityName));

            //host
            activityMarker.add(Markers.append(LoggingKey.HOSTNAME, host));

            //version
            activityMarker.add(Markers.append(LoggingKey.ELFVERSION_KEY, LoggingKey.ELFVERSION_VALUE));


            //method
            activityMarker.add(Markers.append(LoggingKey.METHOD, method.getDeclaringClass().getCanonicalName() + "." + method.getName()));


            if (t != null) {
                activityMarker.add(Markers.appendFields(throwActivityResult));
                loggerStashActivity.error(activityMarker, "Exception in {} for {} ms with exception: {}", activityName, diff, throwActivityResult);
                activityMarker = null;
                mapToLog = null;
                throw t;
            } else {
                if (methodMetaData.isLogResponse()) {
                    activityMarker.add(Markers.appendFields(retVal));
                }
                //Message
                loggerStashActivity.info(activityMarker, "Activity {} processed for {} ms", activityName, diff);


                activityMarker = null;
                mapToLog = null;
                return retVal;
            }
        }finally {
            logContextService.removeActivityLoggingContext();
        }
	}

}
