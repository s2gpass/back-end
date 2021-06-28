package com.kt.safe2go.platform.util;

import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * JsonObject 내 name과 동일한 이름의 프로퍼티가 있을 경우,
 * 첫번째 프로퍼티의 값을 JsonElement로 반환
 * 
 * <pre>
 * com.kt.isearch.gw.util
 * _JsonElementSearcher.java
 * </pre>
 * 
 * @Date 2018. 8. 10.
 */
public class JsonElementSearcher {
	
	public static JsonElement getFirst(JsonObject json, String name) {
		JsonElement elem = null;
		
		for(Entry<String, JsonElement> entry : json.entrySet()) {
			
			if(entry.getValue().isJsonObject()) {
				elem = getFirst(entry.getValue().getAsJsonObject(), name);
				if(elem != null)
					return elem;
			}

			if(entry.getValue().isJsonArray()) {
				JsonArray jsonArray = entry.getValue().getAsJsonArray();
				for(int x = 0; x < jsonArray.size(); x++) {
					elem = getFirst(jsonArray.get(x).getAsJsonObject(), name);
					if(elem != null)
						return elem;
				}
			}

			if(entry.getKey().equals(name))
				return entry.getValue();
		}
		
		return null;
	}
}
