package com.rmv.mse.microengine.logging;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class LoggingServiceTest {

    @Rule
    public final EnvironmentVariables environmentVariables
            = new EnvironmentVariables();


    @Test
    public void getHostName() {
        environmentVariables.set("ELF_HOSTNAME","MyValue");
        LoggingService loggingService=new LoggingService();
        assertThat( loggingService.getHostName(),is("MyValue"));

    }
}