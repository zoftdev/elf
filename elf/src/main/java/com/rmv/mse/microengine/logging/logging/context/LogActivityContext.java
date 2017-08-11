package com.rmv.mse.microengine.logging.logging.context;

import net.logstash.logback.marker.Markers;
import org.slf4j.Marker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoftdev on 8/11/2017.
 */
public class LogActivityContext {
    private Map<String,Object> activityLogMap;
    private Marker activityMarker;

    public Map<String, Object> getActivityLogMap() {
        return activityLogMap;
    }

    private LogActivityContext(Map<String, Object> activityLogMap, Marker activityMarker) {
        this.activityLogMap = activityLogMap;
        this.activityMarker = activityMarker;
    }

    public static LogActivityContext createBasic(){
        return new LogActivityContext(new HashMap<>(), Markers.appendFields(null));
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
}
