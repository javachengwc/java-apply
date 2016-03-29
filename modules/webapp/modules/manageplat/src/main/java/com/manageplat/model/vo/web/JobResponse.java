package com.manageplat.model.vo.web;

public class JobResponse extends BaseResponse{
	
	private Integer key;
	
	private String drive_info;
	
	private Integer drive_result;
	
	private String driver;

	private Object subJobInfo;

    public JobResponse()
    {

    }

    public JobResponse(Integer error,String msg)
    {
        super(error,msg);
    }

	public JobResponse(Integer error, String msg, Integer key) {
		this(error, msg);
		this.key = key;
	}

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

	public String getDrive_info() {
		return drive_info;
	}

	public void setDrive_info(String drive_info) {
		this.drive_info = drive_info;
	}

	public Integer getDrive_result() {
		return drive_result;
	}

	public void setDrive_result(Integer drive_result) {
		this.drive_result = drive_result;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public Object getSubJobInfo() {
		return subJobInfo;
	}

	public void setSubJobInfo(Object subJobInfo) {
		this.subJobInfo = subJobInfo;
	}
	
}
