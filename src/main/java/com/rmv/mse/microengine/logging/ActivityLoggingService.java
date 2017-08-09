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
import com.rmv.mse.microengine.logging.model.ActivityLoggingMessage;
import com.rmv.mse.microengine.logging.model.ActivityResult;
import com.rmv.mse.microengine.logging.model.MethodMetaData;
import com.rmv.mse.microengine.logging.model.TransactionLoggingContext;
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
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ActivityLoggingService {
	ObjectMapper objectMapper=new ObjectMapper();

	@Value("microengine.logging.servicename")
	String servicename;


	@Autowired
    TransactionLoggingContextFactory transactionLoggingContextFactory;

	String host;

	ClassMetaDataCache classMetaDataCache =new ClassMetaDataCache();

	private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Logger loggerStash = LoggerFactory.getLogger("stash");

    @Around("@annotation(com.rmv.mse.microengine.logging.annotation.TransactionLogging)")
    public Object transactionLogging(ProceedingJoinPoint pjp)throws Throwable{
        //clean and regist to mdc;

        Object ret=null;
        try {
             ret = pjp.proceed();
        } finally {

            logger.info("exit tran logging");
        }



        return ret;
    }

	@Around("@annotation(com.rmv.mse.microengine.logging.annotation.ActivityLogging)")
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

        TransactionLoggingContext transactionLoggingContext=transactionLoggingContextFactory.getInFightContext();
        Marker activityMarker= Markers.appendFields(new Object());

        transactionLoggingContext.setActivityMarker(activityMarker);
        transactionLoggingContext.getActivityLogMap().clear();

//        findTransactionLoggingParam(pjp);

        long begin =System.currentTimeMillis();
		try {

                retVal = pjp.proceed();
			//no exception

		} catch (Throwable throwable) {
			throwable.printStackTrace();
            throwActivityResult=new ActivityResult(Error.E77000,"SBM",methodName+" exception:"+throwable.getMessage());
            t=throwable;
	//		throw throwable;
		}
		// stop stopwatch
		long end=System.currentTimeMillis();
		long diff=end-begin;

		ActivityLoggingMessage message=new ActivityLoggingMessage();
		message.setBegin(begin);
		message.setProcessTime(diff);
		message.setActivity(methodName);

		//apply map to marker with priority: retval union marker  union( a map-> t map)
        Map<String,Object> mapToLog=new HashMap<>();
        mapToLog.putAll(transactionLoggingContext.getTransactionLogMap());
        mapToLog.putAll(transactionLoggingContext.getActivityLogMap());
        activityMarker.add(Markers.appendEntries(mapToLog));
        activityMarker.add(transactionLoggingContext.getTransactionMarker());

        activityMarker.add(Markers.appendFields(message));

        if(methodMetaData.isLogResponse()){
            activityMarker.add(Markers.appendFields(retVal));
        }


        if(t !=null){
            activityMarker.add(Markers.appendFields(throwActivityResult));
            loggerStash.error(activityMarker,"Process activity {} for {} ms with {}",methodName,diff,throwActivityResult);
            activityMarker=null;
            mapToLog=null;
            throw  t;
        }
        else{
            loggerStash.info(activityMarker,"Process activity {} for {} ms with {}",methodName,diff,retVal);
            activityMarker=null;
            mapToLog=null;
            return retVal;
        }
	}

}
