package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.logging.annotation.ActivityLogging;
import com.rmv.mse.microengine.logging.logging.model.ActivityResult;
import org.springframework.stereotype.Service;

/**
 * Created by zoftdev on 8/10/2017.
 */
@Service
public class SBMDataServiceSomething {

    @ActivityLogging
    public ActivityResult doSomething() {
        return ActivityResult.SUCCESS;
    }
}
