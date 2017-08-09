package com.rmv.mse.microengine.exampleproject;

import com.rmv.mse.microengine.logging.context.TransactionLoggingContext;
import com.rmv.mse.microengine.logging.context.LoggingContextFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleActivityOnlyTests {
	@Autowired
	private ExampleService service;




	@Test
	public void example_devOnlyActivity() {

		service.doActivity("hlex","password");
		service.doActivity(null,"password");

	}



}
