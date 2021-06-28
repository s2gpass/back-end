package com.kt.safe2go.platform.common.msg;


import lombok.Getter;

@Getter
public enum StatusEnum {
  	
	RET_SUCC(1,"S"),
	RET_FAIL(-1,"F"),
	// 1xx Informational
	
	// 2xx Success
	SUCCESS(200, "성공"),
	
	// 3xx Redirection
	
	// 4xx Client Error
	BAD_REQUEST(400, "The server could not understand the request due to invalid syntax."),
	UNAUTHORIZED(401, "unauthorized"),
	FORBIDDEN (403, "Permission Error."),
	NOT_FOUND (404, "The server can not find the requested resource."),
	
	// 5xx Server Error
	INTERNAL_SERVER_ERROR (500, "Internal Server Error"),
	BAD_GATEWAY (502, "bad gateway Error"),
	SERVICE_UNAVAILABLE (503, "service unavailable Error"),
	GATEWAY_TIMEOUT (504, "gateway timeout Error"),
	
	// ETC
    DB_ERROR(-2, "DB 처리중 오류가 발생하였습니다."),
    NODATA(-3, "데이터가 없습니다."),
    
    // 9xx 공통 필수 관련
    INVALID_PARAMETER(900, "필수 파라메터의 값의 문제가 존재합니다."),
    MISSING_PARAMETER(901, "필수 파라메터가 누락되었습니다."),
	SYSTEM_ERROR(9999,"System failure");
	
    int code;
    String message;

    StatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    

}
