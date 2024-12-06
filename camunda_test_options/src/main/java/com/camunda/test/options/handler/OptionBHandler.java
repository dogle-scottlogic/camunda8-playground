package com.camunda.test.options.handler;
import com.camunda.test.options.BadThingException;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;

import java.util.Map;

public class OptionBHandler implements JobHandler {

	@Override
	public void handle(JobClient client, ActivatedJob job) throws BadThingException {
		
		System.out.println("(" + job.getKey()+ ") Handling job: " + job.getType());

		try{
			final Map<String, Object> inputVariables = job.getVariablesAsMap();
			Integer myVar = (Integer) inputVariables.get("var1");
			System.out.println("List of variables from Zeebe: " + job.getVariables());
			System.out.println("Executing Option B");

			if (myVar == 8) {
				throw new BadThingException();
			}

			client.newCompleteCommand(job.getKey()).send().join();

		}
		catch (BadThingException e) {
			System.out.println("Found a bad");
			client.newThrowErrorCommand(job).errorCode("B_A_D").errorMessage("did a bad").send();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}	
}