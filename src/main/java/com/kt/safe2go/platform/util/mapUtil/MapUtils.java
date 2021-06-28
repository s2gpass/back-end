package com.kt.safe2go.platform.util.mapUtil;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;

public class MapUtils {
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object object, CaseFormat caseFormat) {
		Map<String, Object> map = new ObjectMapper().convertValue(object, Map.class);
		if(caseFormat == null)
			return map;

		Map<String, Object> uppercase = new HashMap<>();
		map.entrySet().forEach(entry -> {
			uppercase.put(CaseFormat.LOWER_CAMEL.to(caseFormat, entry.getKey()), entry.getValue());
		});
		return uppercase;
	}

	public static Map<String, Object> toMap(Object object) {
		return toMap(object, null);
	}
}
