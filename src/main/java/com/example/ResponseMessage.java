package com.example;

import static com.example.ErrorCode.EC_OK;

/**
 * Created by meijun on 2016/11/16.
 */
public class ResponseMessage {
    private ErrorCode errorCode = EC_OK;
    private String msg = "";

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode code) {
        errorCode = code;
    }

    public String getMsg() {
        return  msg;
    }

   public void setMsg(String message) {
       this.msg = message;
   }
}
