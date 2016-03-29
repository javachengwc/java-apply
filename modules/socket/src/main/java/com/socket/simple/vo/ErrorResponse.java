package com.socket.simple.vo;

public class ErrorResponse extends BaseResponse{

    private int type=0;

    private int errorCode;

    private String action;

    private String message;

    public ErrorResponse()
    {

    }

    public ErrorResponse(int errorCode,String message)
    {
        this.errorCode=errorCode;
        this.message=message;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
