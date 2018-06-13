package com.rmv.mse.microengine.logging.context;

import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zoftdev on 8/11/2017.
 */
public class LogActivityContext {
    private Map<String,Object> activityLogMap;
    private Marker activityMarker;
    private String activityId;

    public Map<String, Object> getActivityLogMap() {
        return activityLogMap;
    }

    private LogActivityContext(Map<String, Object> activityLogMap, Marker activityMarker,String activityId) {
        this.activityLogMap = activityLogMap;
        this.activityMarker = activityMarker;
        this.activityId=activityId;
    }

    public static LogActivityContext createBasic(){
        return new LogActivityContext(new HashMap<>(), Markers.appendFields(null), UUID.randomUUID().toString());
    }


    public void setActivityLogMap(Map<String, Object> activityLogMap) {
        this.activityLogMap = activityLogMap;
    }

    public Marker getActivityMarker() {
        return activityMarker;
    }

    public void setActivityMarker(Marker activityMarker) {
        this.activityMarker = activityMarker;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
