package com.z7z8.model;


import org.apache.commons.lang3.builder.ToStringBuilder;

public class QrCodeResponse {

    private Integer error=0;

    private String msg;

	private String filePath;

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

    public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public QrCodeResponse() {
		
	}

    public QrCodeResponse(Integer error,String msg)
    {
        this.error=error;
        this.msg=msg;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


}
