package com.shop.user.api.rest;

import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.base.Rep;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Api(value = "用户接口")
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserResource {

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @POST
    @Path("/getUserInfo")
    public UserInfo getUserInfo(@QueryParam("userId") Long userId);

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @GET
    @Path("/getUserInfo2")
    public Rep<UserInfo> getUserInfo2(@QueryParam("userId") Long userId);
}