package com.kt.safe2go.platform.common.log.vo;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class MaskingVo {
	private boolean enabled;
	private List<String> target;
	private Map<String, String> rule;
}