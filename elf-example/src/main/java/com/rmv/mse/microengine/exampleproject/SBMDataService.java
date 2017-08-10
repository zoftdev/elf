package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.logging.annotation.ActivityLogging;
import com.rmv.mse.microengine.logging.logging.annotation.TransactionLogging;
import com.rmv.mse.microengine.logging.logging.model.ActivityResult;
import com.rmv.mse.microengine.logging.logging.model.TransactionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zoftdev on 8/10/2017.
 */
@Service
public class SBMDataService {

    @Autowired SBMDataServiceSomething sbmDataServiceSomething;

    @TransactionLogging
    public TransactionResult sellPacakage(){

        sbmDataServiceSomething.doSomething();

        return TransactionResult.SUCCESS;
    }


}
