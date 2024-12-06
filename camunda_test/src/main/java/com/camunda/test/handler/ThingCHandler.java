package com.camunda.test.handler;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

import java.util.Map;

public class ThingCHandler implements JobHandler {

	@Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {
		
		System.out.println("(" + job.getKey()+ ") Handling job: " + job.getType());

		try{
			final Map<String, Object> inputVariables = job.getVariablesAsMap();
			System.out.println("List of variables from Zeebe: " + job.getVariables());
			inputVariables.put("var1", 20);
			System.out.println("Executing Thing C");
			client.newCompleteCommand(job.getKey()).variables(inputVariables).send().join();

		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
}