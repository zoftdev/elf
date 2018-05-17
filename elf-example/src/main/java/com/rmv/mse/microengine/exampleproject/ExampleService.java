package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.model.ElfException;
import com.rmv.mse.microengine.logging.prop.LoggingKey;
import com.rmv.mse.microengine.logging.context.LogContext;
import com.rmv.mse.microengine.logging.context.LogContextService;
import com.rmv.mse.microengine.logging.annotation.*;
import com.rmv.mse.microengine.logging.model.ActivityResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {


    @Autowired
    LogContextService logContextService;


    /**
     * Show auto log the response
     */
    @ActivityLog
    public ExampleResult doActivityLogResult() {
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


    @ActivityLog(name="newName")
    public ExampleResult doOverrideName() {
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


    @ActivityLog
    public ExampleResult doException() {
        throw new RuntimeException("test doException");
    }

    @ActivityLog
    public ExampleResult doElfException() {
        throw new ElfException("33","test elf Exception");
    }



    @ActivityLog
    public ExampleResult appendFieldActivityLevel() {
        LogContext logContext = logContextService.getCurrentContext();
        logContext.appendFieldsA(new ObjectWithField());
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


    /**
     * Show manual log the response
     * for some function not prefer to use ActivityResult class
     */
    @ActivityLog(logResponse = false)
    public java.sql.ResultSet doActivityNotLogResponse() {
        LogContext logContext = logContextService.getCurrentContext();
        logContext.appendFieldsA(ActivityResult.SUCCESS);

        return null;
    }


    /**
     * apply member field to tran level
     */
    @ActivityLog
    public ExampleResult appendFieldTranLevel() {
        LogContext logContext = logContextService.getCurrentContext();
        logContext.appendFieldsT(new ObjectWithField());
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }

    /**
     * Example of ActivityLog Logging
     * Class with @ActivityLog will be logged for kinana at the end of method automatically ( AOP ).
     * Support feature:
     *
     * @LogParam log the request parameter
     * <p>
     * MDC: log in thread level
     */
    @ActivityLog
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

    @ActivityLog
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


    @ActivityLog
    public ExampleResult doSetMessage() {
        LogContext context = logContextService.getCurrentContext();
        context.putT(LoggingKey.MESSAGE,"test2");
        return new ExampleResult("0", "SBM", "Success", "3AAAF");
    }


    /** ---------------- Start Lowlevel
     * Stacked Activity support
     * the map of each will separated
     */
    @Autowired  LowLevelService lowLevelService;
    @ActivityLog
    public ActivityResult stackActivity(){
        logContextService.getCurrentContext().putA("from","highlevel");
        lowLevelService.lowLevelActivity();
        return ActivityResult.SUCCESS;
    }

    @Service
    class LowLevelService{

        @Autowired LogContextService mylogContextService;
        @ActivityLog
        public ActivityResult lowLevelActivity(){
            mylogContextService.getCurrentContext().putA("from","lowlevel");
            return ActivityResult.SUCCESS;
        }
    }
    /** ---------------- End Lowlevel */


    @ActivityLog
    public ActivityResult forTestThread(){
        logContextService.getCurrentContext().putA("ThreadNameShouldSeparated",Thread.currentThread().getName());
        return ActivityResult.SUCCESS;
    }

}


