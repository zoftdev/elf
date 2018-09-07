package com.rmv.mse.microengine.logging;

import com.rmv.mse.microengine.logging.annotation.ActivityLog;
import com.rmv.mse.microengine.logging.context.LogContext;
import com.rmv.mse.microengine.logging.context.LogContextService;
import com.rmv.mse.microengine.logging.model.ActivityResult;
import com.rmv.mse.microengine.logging.model.ActivitySlimContext;
import com.rmv.mse.microengine.logging.model.ElfException;
import com.rmv.mse.microengine.logging.model.ExceptionInfo;
import net.logstash.logback.marker.Markers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityLoggingHelper {

    @Autowired
    LogContextService logContextService;

    @ActivityLog(logResponse = false )
    public  void writeActivity(ActivitySlimContext activitySlimContext){
        LogContext logContext = logContextService.getCurrentContext();
        logContext.setOverrideActivityName(activitySlimContext.getActivityName());
        logContext.setOverrideBegin(activitySlimContext.getBegin());
        if(activitySlimContext.getKeyValueMap()!=null){
            for (String key : activitySlimContext.getKeyValueMap().keySet()) {
                logContext.putA(key,activitySlimContext.getKeyValueMap().get(key));
            }
            for(Object o:activitySlimContext.getAppendFields()){
                logContext.appendFieldsA(o);
            }
        }
        if(activitySlimContext.getThrowable()!=null){

             if (activitySlimContext.getThrowable() instanceof ElfException){
                 ElfException elfExcepion= (ElfException) activitySlimContext.getThrowable();
                 elfExcepion.printStackTrace();
                 ActivityResult throwActivityResult = new ActivityResult(elfExcepion.getCode(), "SBM", elfExcepion.getDesc());
                 logContext.appendFieldsA(throwActivityResult);
                 ExceptionInfo exceptionInfo=new ExceptionInfo(elfExcepion);
                 logContext.appendFieldsA(exceptionInfo);


            } else {
                 Throwable throwable=activitySlimContext.getThrowable();
                 ActivityResult throwActivityResult = new ActivityResult(com.rmv.mse.microengine.logging.prop.Error.E77000, "SBM", "Exception:"+throwable.getClass().getSimpleName()+"Message:" + throwable.getMessage());
                 ExceptionInfo exceptionInfo=new ExceptionInfo(throwable);
                 logContext.appendFieldsOnlyT(exceptionInfo);
                 logContext.appendFieldsA(exceptionInfo);
                //		throw throwable;
            }
        }
    }


}
