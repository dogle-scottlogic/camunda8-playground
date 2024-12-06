package com.camunda.test.options.handler;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

import java.util.Map;

public class OptionAHandler implements JobHandler {

	@Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {
		
		System.out.println("(" + job.getKey()+ ") Handling job: " + job.getType());

		try{
			System.out.println("List of variables from Zeebe: " + job.getVariables());
			System.out.println("Executing Option A");
			client.newCompleteCommand(job.getKey()).send().join();

		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
}