package com.kt.safe2go.platform.common.log.vo;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionVo {
	private String transactionId;
	private String api;
	private String apiKey;
	private String clientIp;
	private long startTime;
	private long endTime;
	private int status;
	private String returnCode;
	private String errorCode;
	private String errormessage;
}

