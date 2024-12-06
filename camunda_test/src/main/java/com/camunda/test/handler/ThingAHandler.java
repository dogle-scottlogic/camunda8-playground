package com.camunda.test.handler;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

import java.util.Map;

public class ThingAHandler implements JobHandler {

	@Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {
		
		System.out.println("(" + job.getKey()+ ") Handling job: " + job.getType());

		try{
			final Map<String, Object> inputVariables = job.getVariablesAsMap();
			System.out.println("List of variables from Zeebe: " + job.getVariables());
			System.out.println("Executing Thing A");
			client.newCompleteCommand(job.getKey()).variables(inputVariables).send().join();

		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
}