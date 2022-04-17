package com.httpclient.openapi.utils;


import com.alibaba.fastjson.JSONObject;

public class Response {
    private int status;
    private StringBuilder body;

    public Response() {
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public StringBuilder getBody() {
        return this.body;
    }

    public void setBody(StringBuilder body) {
        this.body = body;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("status", this.status);
        json.put("body", this.body);
        return json;
    }
}
