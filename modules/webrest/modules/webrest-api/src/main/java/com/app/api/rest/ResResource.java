package com.app.api.rest;

import com.app.model.Resource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/rest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "ResResource相关接口")
public interface ResResource {

    //查询记录
    @GET
    @Path("/{id}/resource")
    @ApiOperation(value = "getResource", notes = "getResource")
    public Resource getResource(@PathParam("id") Integer id);

    @GET
    @Path("/resource")
    @ApiOperation(value = "queryByName", notes = "queryByName")
    public Resource queryByName(@QueryParam("name") String name);
}
