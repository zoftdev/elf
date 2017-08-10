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

package com.rmv.mse.microengine.logging.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmv.mse.microengine.logging.logging.context.TransactionLoggingContextFactory;
import com.rmv.mse.microengine.logging.logging.model.ActivityResult;
import com.rmv.mse.microengine.logging.logging.model.ClassMetaData;
import com.rmv.mse.microengine.logging.logging.model.MethodMetaData;
import com.rmv.mse.microengine.logging.logging.context.TransactionLoggingContext;
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
    TransactionLoggingContextFactory transactionLoggingContextFactory;

	String host;

	ClassMetaDataCache classMetaDataCache =new ClassMetaDataCache();

	private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Logger loggerStash = LoggerFactory.getLogger("stash");

    //todo message

    @Around("@annotation(com.rmv.mse.microengine.logging.logging.annotation.TransactionLogging)")
    public Object transactionLogging(ProceedingJoinPoint pjp)throws Throwable{

        //func name
        String methodName=pjp.getSignature().getName();
        Class c=pjp.getSignature().getDeclaringType();
        classMetaDataCache.initialize(c);
        ClassMetaData classMetaData = classMetaDataCache.getCachedClass().get(c);
        MethodMetaData methodMetaData = classMetaData.getTransactionMethod().get(methodName);

        //create context
        TransactionLoggingContext context = transactionLoggingContextFactory.addTransactionLoggingContext(TransactionLoggingContext.getDummy());
        Marker marker = context.getTransactionMarker();
        Throwable t=null;
        Object ret=null;
        long begin=System.currentTimeMillis();

        try {
             ret = pjp.proceed();
            if(methodMetaData.isLogResponse())
                marker.add(Markers.appendFields(ret));
        } catch (Throwable throwable){
            logger.error("Error in Process {}",throwable.getMessage(),throwable);
            t=throwable;
            marker.add(Markers.appendFields(new ActivityResult(Error.E77000,"SBM","Exception:"+throwable.getMessage())));

        }
        long processTime=System.currentTimeMillis()-begin;

        //type
        marker.add(Markers.append(LoggingKey.TYPE,LoggingKey.TRANSACTION));

        //process name
        String processName=methodName;
        if(methodMetaData.isOverrideName()){
            processName=methodMetaData.getOverrideName();
        }
        marker.add(Markers.append(LoggingKey.PROCESS,processName));

        //time , begin
        marker.add(Markers.append(LoggingKey.PROCESS_TIME,processTime));
        marker.add(Markers.append(LoggingKey.BEGIN,begin));

        //id
        marker.add(Markers.append(LoggingKey.TRANSACTIONID,context.getTransactionId()));

        //map
        marker.add(Markers.appendEntries(context.getTransactionLogMap()));

        //parent
        if(context.getParentTransactionId()!=null){
            marker.add(Markers.append(LoggingKey.PARTENT_TRANSACTION_ID, context.getParentTransactionId()));
        }


        loggerStash.info(marker,"Process {} Finish in {} ms",processTime);

        //result
        if(t!=null){
            throw  t;
        }else {
            return ret;
        }
    }

    //todo overlap service
	@Around("@annotation(com.rmv.mse.microengine.logging.logging.annotation.ActivityLogging)")
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
        TransactionLoggingContext context=transactionLoggingContextFactory.getInFightContext();
        Marker activityMarker= Markers.appendFields(new Object());
        context.setActivityMarker(activityMarker);
        context.getActivityLogMap().clear();

//        findTransactionLoggingParam(pjp);

        long begin =System.currentTimeMillis();
		try {

                retVal = pjp.proceed();
			//no doException

		} catch (Throwable throwable) {
			throwable.printStackTrace();
            throwActivityResult=new ActivityResult(Error.E77000,"SBM","Exception:"+throwable.getMessage());
            t=throwable;
	//		throw throwable;
		}
		// stop stopwatch
		long end=System.currentTimeMillis();
		long diff=end-begin;



		String activityName=methodName;
		if(methodMetaData.isOverrideName()) {
            activityName = methodMetaData.getOverrideName();
        }



		//apply map to marker with priority: retval union marker  union( a map-> t map)
        Map<String,Object> mapToLog=new HashMap<>();
        mapToLog.putAll(context.getTransactionLogMap());
        mapToLog.putAll(context.getActivityLogMap());
        activityMarker.add(Markers.appendEntries(mapToLog));
        activityMarker.add(context.getTransactionMarker());


        activityMarker.add(Markers.append(LoggingKey.TYPE,LoggingKey.ACTIVITY));
        //id
        activityMarker.add(Markers.append(LoggingKey.TRANSACTIONID,context.getTransactionId()));;
        activityMarker.add(Markers.append(LoggingKey.PROCESS_TIME,diff));
        activityMarker.add(Markers.append(LoggingKey.BEGIN,begin));
        activityMarker.add(Markers.append(LoggingKey.ACTIVITY,activityName));

        //method
        activityMarker.add(Markers.append(LoggingKey.METHOD,method.getDeclaringClass().getCanonicalName()+"."+method.getName()));




        if(t !=null){
            activityMarker.add(Markers.appendFields(throwActivityResult));
            loggerStash.error(activityMarker,"Exception in {} for {} ms with exception: {}",activityName,diff,throwActivityResult);
            activityMarker=null;
            mapToLog=null;
            throw  t;
        }
        else{
            if(methodMetaData.isLogResponse()){
                activityMarker.add(Markers.appendFields(retVal));
            }
            //Message
            loggerStash.info(activityMarker, "Process {} for {} ms", activityName, diff);


            activityMarker=null;
            mapToLog=null;
            return retVal;
        }
	}

}
