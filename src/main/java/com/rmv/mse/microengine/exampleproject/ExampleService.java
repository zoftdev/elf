package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.annotation.*;
import com.rmv.mse.microengine.logging.model.ActivityResult;
import net.logstash.logback.marker.Markers;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ExampleService {


    /**
     * Show auto log the response
     */
    @Activity
    public ExampleResult doActivityLogResult() {
        return new ExampleResult("0","SBM","Success","3AAAF");
    }


    /**
     * Show manual log the response
     * for some function not prefer to use ActivityResult class
     */
    @Activity
    @NotLogResponse
    public boolean doActivityNotLogResponse(@ActivityMarker Marker marker) {
        marker.add(Markers.appendFields(new ActivityResult("0","SBM","Success")));
        return true;
    }




    /**
     *  Example of Activity Logging
     *  Class with @Activity will be logged for kinana at the end of method automatically ( AOP ).
     *  Support feature:
     *      @LogParam log the request parameter
     *      @ActivityMarker : log marker in activity level : see slf4j's marker
     *      @TransactionMarker : log marker in transaction level
     *      MDC: log in thread level
     */
    @Activity
    public ExampleResult exampleLogging(@LogParam("name") String name,String password,
                                    @ActivityMarker Marker marker,
                                    @TransactionMarker Marker transactionMarker) {
        //put to transaction level
        MDC.put("ip","172.0.1.1");

        //put to activity level
        marker.add(Markers.append("key","value"));
        marker.add(Markers.appendArray("key","this","array","will","be","added"));

        HashMap<String,String> map=new HashMap<>();
        map.put("k1","v1");
        marker.add(Markers.appendEntries(map));

        marker.add(Markers.appendArray("key","this","array","will","be","added"));
        marker.add(Markers.appendRaw("I","{\"am\":\"json\"}"));

        transactionMarker.add(Markers.append("IMSI","5200010000"));

        //dosomething

        //return field will logged (exclude by @Nolog : check ExampleResult class)
        return new ExampleResult("0", "SBM", "This is a test", "111");
    }










}


