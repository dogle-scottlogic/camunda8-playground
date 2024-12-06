package com.camunda.academy.camunda_spring;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.spring.client.annotation.JobWorker;

import java.util.Map;

@Component
public class ThingWorker {
    private static final long WORKER_TIMEOUT = 150000L;

    @JobWorker(type = "thingA", timeout = WORKER_TIMEOUT)
    public void doThingA(@Variable(name = "var1") Integer var1, final JobClient client, final ActivatedJob job) {
        doAThing(client, job);
    }

    @JobWorker(type = "thingB", timeout = WORKER_TIMEOUT)
    public void doThingB(final JobClient client, final ActivatedJob job) {
        doAThing(client, job);
    }

    @JobWorker(type = "thingC", timeout = WORKER_TIMEOUT)
    public void doThingC(final JobClient client, final ActivatedJob job) {
        doAThing(client, job);
    }

    @JobWorker(type = "thingD", timeout = WORKER_TIMEOUT)
    public void doThingD(final JobClient client, final ActivatedJob job) {
        try {
            doAThing(client, job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JobWorker(type = "bad", timeout = WORKER_TIMEOUT)
    public void bad(final JobClient client, final ActivatedJob job) {
        System.out.println("(" + job.getKey()+ ") Handling job: " + job.getType());

        try{
            System.out.println("You did a bad, do not do the bad again please!");
            client.newCompleteCommand(job.getKey()).send().join();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void doAThing(final JobClient client, final ActivatedJob job) {
        System.out.println("(" + job.getKey()+ ") Handling job: " + job.getType());
        try{
            final Map<String, Object> inputVariables = job.getVariablesAsMap();
            System.out.println("List of variables from Zeebe: " + job.getVariables());
            Thread.sleep(30000);
            System.out.println("Job " +job.getType() + " completed!");
            client.newCompleteCommand(job.getKey()).variables(inputVariables).send().join();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
