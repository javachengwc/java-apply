package com.app.model.vo;

public class DefRsp extends RspData<String> {

    public DefRsp(Integer result,String msg)
    {
        super();
        setResult(result);
        setMsg(msg);
        setData("");
    }
}
