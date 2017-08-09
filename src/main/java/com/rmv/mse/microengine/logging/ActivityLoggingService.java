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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ActivityLoggingService {
	ObjectMapper objectMapper=new ObjectMapper();

	@Value("microengine.logging.servicename")
	String servicename;


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
        int posOfTransactionLogging = methodMetaData.getPosOfTransactionLogging();
        TransactionLoggingContext transactionLoggingContext=null;
        Marker activityMarker= Markers.appendFields(null);
        boolean modify_param=false;
        if(posOfTransactionLogging>=0){
            modify_param=true;
            transactionLoggingContext= (TransactionLoggingContext) pjp.getArgs()[posOfTransactionLogging];
            transactionLoggingContext.setActivityMarker(activityMarker);
            transactionLoggingContext.getActivityLogMap().clear();
        }else{

        }
//        findTransactionLoggingParam(pjp);

        long begin =System.currentTimeMillis();
		try {
		    if(modify_param){
		        retVal=pjp.proceed(pjp.getArgs());
            }
            else
                retVal = pjp.proceed();
			//no exception

		} catch (Throwable throwable) {
			throwable.printStackTrace();
            throwActivityResult=new ActivityResult(Error.E77000,"SBM",methodName+" exception:"+throwable.getMessage());
	//		throw throwable;
		}
		// stop stopwatch
		long end=System.currentTimeMillis();
		long diff=end-begin;

		ActivityLoggingMessage message=new ActivityLoggingMessage();
		message.setBegin(begin);
		message.setProcessTime(diff);
		message.setActivity(methodName);

		//apply map to marker with priority: t->a->marker
        if(transactionLoggingContext!=null) {
            activityMarker.add(Markers.appendEntries(transactionLoggingContext.getTransactionLogMap()));
            activityMarker.add(Markers.appendEntries(transactionLoggingContext.getActivityLogMap()));
        }
        activityMarker.add(Markers.appendFields(message));
        if(methodMetaData.isLogResponse()){
            activityMarker.add(Markers.appendFields(retVal));
        }


        if(t !=null){
            loggerStash.error(activityMarker,"Process activity {} for {} ms with {}",methodName,diff,throwActivityResult,t);
            throw  t;
        }
        else{
            loggerStash.info(activityMarker,"Process activity {} for {} ms with {}",methodName,diff,retVal);
            return retVal;
        }
	}

}
