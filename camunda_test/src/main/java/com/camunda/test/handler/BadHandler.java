package com.camunda.test.handler;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

import java.util.Map;

public class BadHandler implements JobHandler {

	@Override
	public void handle(JobClient client, ActivatedJob job) throws Exception {
		
		System.out.println("(" + job.getKey()+ ") Handling job: " + job.getType());

		try{
			System.out.println("You did a bad, do not do the bad again please!");
			client.newCompleteCommand(job.getKey()).send().join();

		}catch(Exception e){
			e.printStackTrace();
		}		
	}	
}