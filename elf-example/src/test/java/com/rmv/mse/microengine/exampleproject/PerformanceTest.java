package com.rmv.mse.microengine.exampleproject;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class PerformanceTest {

//    @Rule
//    public  ContiPerfRule i=new ContiPerfRule();

    @Autowired
    ExampleTransaction exampleTransaction;


    @Test
//    @PerfTest(invocations = 10000,threads = 4)
    public void example_hello_world() throws Exception {

        while (true){
//        for (int j = 0; j <500000 ; j++) {
            try {
                exampleTransaction.deepService();
            }catch (Throwable t){
                
            }
        }
     //   Thread.sleep(1000000);

    }
}
