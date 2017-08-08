package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.LoggingKey;

import com.rmv.mse.microengine.logging.annotation.Activity;
import com.rmv.mse.microengine.logging.annotation.LogMDC;
import com.rmv.mse.microengine.logging.annotation.LogParam;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExampleService {


    /**
     *  Exampe of generic logging
     */
    @Activity
    public ExampleResult doActivity(@LogParam("name") String name,String password) {
        //dosomething
        return new ExampleResult("0", "SBM", "This is a test", "111");
    }


    @Activity
    public String doBasic(String name) {

        return "ok";
    }

    @Activity
    public String doLogMDC(String name, @LogMDC Map<String,Object> map) {
        map.put("mapkey","value");
        return "ok";
    }



}


