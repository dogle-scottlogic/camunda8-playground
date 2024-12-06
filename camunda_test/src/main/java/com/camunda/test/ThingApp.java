package com.camunda.test;

import com.camunda.test.handler.*;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Scanner;

public class ThingApp {

    // Zeebe Client Credentials
    private static final String ZEEBE_PROPERTIES_PATH = "src/main/resources/application.properties";
    private static String ZEEBE_ADDRESS;
    private static String ZEEBE_CLIENT_ID;
    private static String ZEEBE_CLIENT_SECRET;
    private static final String ZEEBE_TOKEN_AUDIENCE = "zeebe.camunda.io";

    private static final String PROCESS_ID = "Process_08agtnw";
    private static final int NUM_INSTANCES = 1;
    private static final int WORKER_TIMEOUT = 15;

    public static void main(String[] args) {
        loadProperties();
        final OAuthCredentialsProvider credentialsProvider = new OAuthCredentialsProviderBuilder()
                .audience(ZEEBE_TOKEN_AUDIENCE)
                .clientId(ZEEBE_CLIENT_ID)
                .clientSecret(ZEEBE_CLIENT_SECRET)
                .build();

        try (final ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress(ZEEBE_ADDRESS)
                .credentialsProvider(credentialsProvider)
                .build()) {

//            startProcessInstances(client, NUM_INSTANCES);

            final JobWorker thingAWorker = client.newWorker()
                    .jobType("thingA")
                    .handler(new ThingAHandler())
                    .timeout(Duration.ofSeconds(WORKER_TIMEOUT).toMillis())
                    .fetchVariables("var1")
                    .open();

            final JobWorker thingBWorker = client.newWorker()
                    .jobType("thingB")
                    .handler(new ThingBHandler())
                    .timeout(Duration.ofSeconds(WORKER_TIMEOUT).toMillis())
                    .open();

            final JobWorker thingCWorker = client.newWorker()
                    .jobType("thingC")
                    .handler(new ThingCHandler())
                    .timeout(Duration.ofSeconds(WORKER_TIMEOUT).toMillis())
                    .open();

            final JobWorker thingDWorker = client.newWorker()
                    .jobType("thingD")
                    .handler(new ThingDHandler())
                    .timeout(Duration.ofSeconds(WORKER_TIMEOUT).toMillis())
                    .open();

            final JobWorker errorWorker = client.newWorker()
                    .jobType("bad")
                    .handler(new BadHandler())
                    .timeout(Duration.ofSeconds(WORKER_TIMEOUT).toMillis())
                    .open();

            // Terminate the worker with an Integer input
            Scanner sc = new Scanner(System.in);
            sc.nextInt();
            sc.close();
            thingAWorker.close();
            thingBWorker.close();
            thingCWorker.close();
            thingDWorker.close();
            errorWorker.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startProcessInstances(ZeebeClient zeebeClient, int numInstances) {

        System.out.println(PROCESS_ID + ": Starting "+ numInstances + " process instances...");
        for (int i = 0; i < numInstances; i++) {
                    zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId(PROCESS_ID)
                    .latestVersion()
//                    .variables(fakeRequest.getRandom())
                    .send()
                    .join();
        }
        System.out.println("... " + numInstances + " instances created");
    }

    private static void loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(ZEEBE_PROPERTIES_PATH)) {
            properties.load(input);
            ZEEBE_ADDRESS = properties.getProperty("zeebe.client.cloud.address");
            ZEEBE_CLIENT_ID = properties.getProperty("zeebe.client.cloud.clientId");
            ZEEBE_CLIENT_SECRET = properties.getProperty("zeebe.client.cloud.clientSecret");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
