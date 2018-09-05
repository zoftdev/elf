package com.rmv.mse.microengine.logging;

import com.rmv.mse.microengine.logging.annotation.ActivityLog;
import com.rmv.mse.microengine.logging.context.LogContext;
import com.rmv.mse.microengine.logging.context.LogContextService;
import com.rmv.mse.microengine.logging.model.ActivitySlimContext;
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
    }


}
