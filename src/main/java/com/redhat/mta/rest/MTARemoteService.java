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
package com.redhat.mta.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.redhat.mta.types.Application;
import com.redhat.mta.types.TaskGroup;

@Path("/hub")
@RegisterRestClient
public interface MTARemoteService {

    @GET
    @Path("/applications")
    @Produces("application/json")
    List<Application> getAll();

    @POST
    @Path("/applications")
    @Produces("application/json")
    Application create(Application application);


    @GET
    @Path("/taskgroups/{id}")
    @Produces("application/json")
    TaskGroup get(@PathParam("id") int id);

    @POST
    @Path("/taskgroups")
    @Produces("application/json")
    TaskGroup create(TaskGroup taskgroup);

    @PUT
    @Path("/taskgroups/{id}/submit")
    @Produces("application/json")
    void submit(@PathParam("id") int id, TaskGroup tg);

}
