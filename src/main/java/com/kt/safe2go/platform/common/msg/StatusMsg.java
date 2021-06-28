package com.kt.safe2go.platform.common.msg;


import lombok.Data;

@Data
public class StatusMsg {

    private String returnCode;
    private int errorCode;
    private String errorMessage;
    private Object data;
//
//    public StatusMsg() {
//        this.returnCode="F";
//        this.errorCode = StatusEnum.BAD_REQUEST.getCode();
//        this.errorMessage = StatusEnum.BAD_REQUEST.getMessage();
//        this.data = null;
//    }
}
