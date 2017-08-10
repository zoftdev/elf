package com.rmv.mse.microengine.exampleproject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by zoftdev on 8/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpamServiceTest {

    @Autowired SpamService spamService;

    @Test
    public void buyPackage() throws Exception {
        MDC.put("hlex","ja");
        spamService.buyPackage();
    }


}