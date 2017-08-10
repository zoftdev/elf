package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.logging.LoggingKey;
import com.rmv.mse.microengine.logging.logging.context.LogContext;
import com.rmv.mse.microengine.logging.logging.context.LogContextService;
import com.rmv.mse.microengine.logging.logging.annotation.*;
import com.rmv.mse.microengine.logging.logging.model.ActivityResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {


    @Autowired
    LogContextService logContextService;


    /**
     * Show auto log the response
     */
    @ActivityLogging
    public ExampleResult doActivityLogResult() {
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


    @ActivityLogging(name="newName")
    public ExampleResult doOverrideName() {
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


    @ActivityLogging
    public ExampleResult doException() {
        throw new RuntimeException("test doException");
    }


    @ActivityLogging
    public ExampleResult appendFieldActivityLevel() {
        LogContext logContext = logContextService.getCurrentContext();
        logContext.appendFieldsA(new ObjectWithField());
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


    /**
     * Show manual log the response
     * for some function not prefer to use ActivityResult class
     */
    @ActivityLogging(logResponse = false)
    public java.sql.ResultSet doActivityNotLogResponse() {
        LogContext logContext = logContextService.getCurrentContext();
        logContext.appendFieldsA(ActivityResult.SUCCESS);

        return null;
    }


    /**
     * apply member field to tran level
     */
    @ActivityLogging
    public ExampleResult appendFieldTranLevel() {
        LogContext logContext = logContextService.getCurrentContext();
        logContext.appendFieldsT(new ObjectWithField());
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
        LogContext logContext = logContextService.getCurrentContext();
        //transaction level
        logContext.putT(LoggingKey.IMSI, "5200099998888");
        //activity level
        logContext.putA("am_request_id", "22222");

        //dosomething

        //return field will logged (exclude by @Nolog : check ExampleResult class)
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }

    @ActivityLogging
    public ActivityResult foo() {
        return ActivityResult.SUCCESS;
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


    @ActivityLogging
    public ExampleResult doSetMessage() {
        LogContext context = logContextService.getCurrentContext();
        context.putT(LoggingKey.MESSAGE,"test2");
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


}


