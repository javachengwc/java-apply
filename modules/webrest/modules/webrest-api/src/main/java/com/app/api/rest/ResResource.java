package com.app.api.rest;

import com.app.model.Resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/rest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ResResource {

    //查询记录
    @GET
    @Path("/{id}/resource")
    public Resource getResource(@PathParam("id") Integer id);

    @GET
    @Path("/resource")
    public Resource queryByName(@QueryParam("name") String name);
}
