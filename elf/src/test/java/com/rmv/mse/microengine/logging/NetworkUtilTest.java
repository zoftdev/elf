package com.rmv.mse.microengine.logging;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class NetworkUtilTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void getHostByName() {
        logger.info(new NetworkUtil().getHostByName());
    }

}