package com.app.api.rest;

import com.app.model.Record;
import com.app.model.vo.RspData;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/rrest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RestResource {

    //默认返回结果
    @GET
    @Path("/")
    public RspData<String> def();

    //查询状态
    @GET
    @Path("/status")
    public RspData<Map<String,Object>> status();

    //增加记录
    @POST
    @Path("/genRecord")
    public  RspData<Integer>  genRecord(Record record);

    //查询记录
    @GET
    @Path("/{id}/record")
    public  Record  getRecord(@PathParam("id") String recordId);

    //查询记录
    @GET
    @Path("/queryRecord")
    public  Record  queryRecord(@QueryParam("id") String recordId);

    //检验rest get方法是否支持model传参，结果是不支持，此接口没办法传参
    @GET
    @Path("/queryRecordByRecord")
    public  Record  queryRecordByRecord(Record record);
}
