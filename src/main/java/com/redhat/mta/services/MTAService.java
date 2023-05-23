/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.mta.services;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.redhat.mta.rest.MTARemoteService;
import com.redhat.mta.types.Application;
import com.redhat.mta.types.Task;
import com.redhat.mta.types.TaskGroup;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MTAService {

    @Inject
    @RestClient
    MTARemoteService mtaRemoteService;

    @Fallback(fallbackMethod = "missingApp")
    public Application get(String applicationName) {

        System.out.printf("going to use mta remote service for the call - application name %s\n", applicationName);

        System.out.printf("remote service %s", mtaRemoteService);

        try {
            var apps = mtaRemoteService.getAll();
            System.out.println("apps " + apps);

            return apps.stream().filter(a -> a.name().equals(applicationName)).findAny().orElse(null);
        } catch (Exception e) {
            System.out.println(e);
        }
       
        return null;


    }

    public Application missingApp(String username) {
        return null;
    }


    public TaskGroup create(Application application) {
        System.out.println("the application id " + application.id());
        var newTG = TaskGroup.ofCloudReadiness(application.id());
        return mtaRemoteService.create(newTG);
    }

    public TaskGroup submit(TaskGroup taskGroup) {
        System.out.println("the taskgroup " + taskGroup);
        mtaRemoteService.submit(taskGroup.id(), taskGroup);
        return taskGroup;
    }

    public TaskGroup get(TaskGroup taskGroup) {
        return mtaRemoteService.get(taskGroup.id());
    }

    public void sleep(int millis) {
        try {
            System.out.printf("sleeping for %d millis\n", millis);
            Thread.sleep(millis);
        } catch (Exception e){
            System.out.println("some sleep error occured " + e);
        }
    }

    public TaskGroup waitTillDone(TaskGroup taskGroup) {
        while(true) {
            var tg = mtaRemoteService.get(taskGroup.id());
            if ("Ready".equals(tg.state())
                && tg.tasks() != null
				&& "Succeeded".equals(tg.tasks()[0].state())) {
                    System.out.println("task is done");
                    return tg;
            } else {
                sleep(1000);
            }
        }

    }
}
