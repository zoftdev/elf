package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.LoggingKey;
import com.rmv.mse.microengine.logging.TransactionLoggingContextFactory;
import com.rmv.mse.microengine.logging.annotation.*;
import com.rmv.mse.microengine.logging.model.TransactionLoggingContext;
import com.rmv.mse.microengine.logging.model.ActivityResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;

@Service
public class ExampleService {


    @Autowired
    TransactionLoggingContextFactory transactionLoggingContextFactory;


    /**
     * Show auto log the response
     */
    @ActivityLogging
    public ExampleResult doActivityLogResult() {
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


    @ActivityLogging
    public ExampleResult doException() {
        throw new RuntimeException("test exception");
    }


    @ActivityLogging
    public ExampleResult appendFieldActivityLevel() {
        TransactionLoggingContext transactionLoggingContext = transactionLoggingContextFactory.getInFightContext();
        transactionLoggingContext.appendActivityFields(new ObjectWithField());
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


    /**
     * Show manual log the response
     * for some function not prefer to use ActivityResult class
     */
    @ActivityLogging
    @NotLogResponse
    public ResultSet doActivityNotLogResponse() {
        TransactionLoggingContext transactionLoggingContext = transactionLoggingContextFactory.getInFightContext();
        transactionLoggingContext.appendActivityFields(ActivityResult.SUCCESS);

        return null;
    }


    /**
     * apply member field to tran level
     */
    @ActivityLogging
    public ExampleResult appendFieldTranLevel() {
        TransactionLoggingContext transactionLoggingContext = transactionLoggingContextFactory.getInFightContext();
        transactionLoggingContext.appendTransactionFields(new ObjectWithField());
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }

    /**
     * Example of ActivityLogging Logging
     * Class with @ActivityLogging will be logged for kinana at the end of method automatically ( AOP ).
     * Support feature:
     *
     * @LogParam log the request parameter
     * <p>
     * MDC: log in thread level
     */
    @ActivityLogging
    public ExampleResult exampleLogging(
            String name,
            String password

    ) {
        TransactionLoggingContext transactionLoggingContext = transactionLoggingContextFactory.getInFightContext();
        //transaction level
        transactionLoggingContext.getTransactionLogMap().put(LoggingKey.IMSI, "5200099998888");
        //activity level
        transactionLoggingContext.getActivityLogMap().put("am_request_id", "22222");

        //dosomething

        //return field will logged (exclude by @Nolog : check ExampleResult class)
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }

    class ObjectWithField {
        int field1 = 5;
        String field2 = "hahaa";

        public int getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }
    }
}


