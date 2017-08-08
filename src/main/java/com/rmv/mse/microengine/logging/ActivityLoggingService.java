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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rmv.mse.microengine.logging.annotation.Transaction;
import com.rmv.mse.microengine.logging.context.TransactionLoggingContext;
import com.rmv.mse.microengine.logging.exception.ActivityLoggingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ActivityLoggingService {
	ObjectMapper objectMapper=new ObjectMapper();

	@Value("microengine.logging.servicename")
	String servicename;

    MDCLinkedHashMap mdcLinkedHashMap=new MDCLinkedHashMap();

	String host;

	ClassMetaDataCache classMetaDataCache =new ClassMetaDataCache();

	private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Logger loggerStash = LoggerFactory.getLogger("stash");

    @Around("@annotation(com.rmv.mse.microengine.logging.annotation.Transaction)")
    public Object transactionLogging(ProceedingJoinPoint pjp)throws Throwable{
        //clean and regist to mdc;
        mdcLinkedHashMap.remove(Thread.currentThread());
        mdcLinkedHashMap.put(Thread.currentThread(),new TransactionLoggingContext());
        Object ret=null;
        try {
             ret = pjp.proceed();
        } finally {
            mdcLinkedHashMap.remove(Thread.currentThread());
            logger.info("exit tran logging");
        }



        return ret;
    }

	@Around("@annotation(com.rmv.mse.microengine.logging.annotation.Activity)")
	public Object activityLogging(ProceedingJoinPoint pjp) throws Throwable {
		// start stopwatch
		Object retVal = null;
        String methodName=pjp.getSignature().getName();
        Class c=pjp.getSignature().getDeclaringType();
        classMetaDataCache.initialize(c);
        Method method = classMetaDataCache.getActivtyMethod(c,methodName);
        Map<String, Object> annotatedParameterValue =classMetaDataCache.getAnnotatedParameterValue(c,methodName, pjp.getArgs());

        ClassMetaData classMetaData = classMetaDataCache.getCachedClass().get(c);
        MethodMetaData methodMetaData = classMetaData.getActivtyMethod().get(methodName);

        boolean mdcFound=false;
        boolean mdcOnThread=false;
        Map mdc=null;
        //check MDC position in param
        if(methodMetaData.getPosOfLogMDC()>=0){
            mdcFound=true;

            //check thread MDC?
            TransactionLoggingContext transactionLoggingContext = mdcLinkedHashMap.get(Thread.currentThread());

            if(transactionLoggingContext==null){
                //localMDC
                mdc=new HashMap();
            }else{
                //thread MDC
                mdc=transactionLoggingContext.getMap();
                mdcOnThread=true;
            }

            pjp.getArgs()[methodMetaData.getPosOfLogMDC()]=mdc;
        }

//        Map<String, Object> annotatedParameterValue = getAnnotatedParameterValue(method, pjp.getArgs());
        long begin =System.currentTimeMillis();
		try {
            retVal = pjp.proceed(pjp.getArgs());
			//no exception

		} catch (Throwable throwable) {
			throwable.printStackTrace();
			//have exception
            //TODO
			throw throwable;
		}
		// stop stopwatch
		long end=System.currentTimeMillis();
		long diff=end-begin;

		ActivityLoggingMessage message=new ActivityLoggingMessage();
		message.setBegin(begin);

		message.setDiff(diff);
		message.setRequest(annotatedParameterValue);
		message.setResponse(retVal);
		message.setActivity(methodName);
        loggerStash.info()
//		String messageString=objectMapper.writeValueAsString(message);
//        JsonNode jsonNode = objectMapper.convertValue(message, JsonNode.class);






//        messageString=jsonNode.toString();


//        objectMapper.writeValue(g,testMap);
        if(mdc!=null)
		logger.info("mdc: {}",mdc);
		logger.info("activityLogging: {}",message);

		if(!mdcOnThread){
		    mdc=null;
        }

		return retVal;
	}

}
