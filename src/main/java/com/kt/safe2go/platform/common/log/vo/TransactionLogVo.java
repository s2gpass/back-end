package com.kt.safe2go.platform.common.log.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionLogVo {
	private String sysId;
	private String applicationId;
	private String currentTime;
	private String hostname;
	private String srvrIp;
	private String remoteIp;
	private String apiKey;
	private String classPath;
	private String txId;
	private String api;
	private String allow;
	private String resultCode;
	private String errLevel;
	private String errCode;
	private String errMessage;
	private String payload;
}
