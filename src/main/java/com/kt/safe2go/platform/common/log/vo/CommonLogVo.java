package com.kt.safe2go.platform.common.log.vo;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "common-log")
public class CommonLogVo {
	private Map<String, Object> properties;
	private MaskingVo masking;
	private Map<String, List<String>> bodyType;
	private List<String> xmlResUnchecked;
	private List<String> txResBodyNotLogging;
}