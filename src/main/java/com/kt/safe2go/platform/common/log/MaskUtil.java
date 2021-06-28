package com.kt.safe2go.platform.common.log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kt.safe2go.platform.common.exception.S2GRuntimeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MaskUtil {
	private static MaskUtil singleton = new MaskUtil();

	// mask all characters
	public static final int MASK_TYPE_ALL = 20;
	// mask eventh characters
	public static final int MASK_TYPE_EVENTH = 21;
	// mask fixed-length characters
	public static final int MASK_TYPE_FIXED = 23;

	// Mask rules cache object
	private static Map<String, Integer> maskRules = null;

	private MaskUtil() {
	}

	public static MaskUtil getInstance() {
		return singleton;
	}

	/**
	 * Mask rule setting
	 * 
	 * @param ruleMap
	 * 
	 * @throws Exception
	 */
	public void parseRules(Map<String, String> ruleMap) throws S2GRuntimeException {
		maskRules = loadRules(ruleMap);

		if (maskRules == null) { throw new S2GRuntimeException("Mask Rules are not loaded"); }
	}

	/**
	 * Mask rule setting
	 * 
	 * @param ruleMap
	 * 
	 * @return loadedRules
	 */
	private Map<String, Integer> loadRules(Map<String, String> ruleMap) {
		log.info("MaskUtil start loading");

		Map<String, Integer> loadedRules = new HashMap<String, Integer>();

		try {
			for (String key : ruleMap.keySet()) {

				int maskType = -1;
				if ("all".equals(ruleMap.get(key))) maskType = MASK_TYPE_ALL;
				else if ("eventh".equals(ruleMap.get(key))) maskType = MASK_TYPE_EVENTH;
				else if ("fixed".equals(ruleMap.get(key))) maskType = MASK_TYPE_FIXED;

				if (loadedRules.containsKey(key)) {
					log.info("MaskUtil : Rule[" + key + "] is duplicate");
				}

				loadedRules.put(key, maskType);

			}

			log.info("MaskUtil loaded " + loadedRules.size() + " rules from config");

			return loadedRules;
	
    	} catch (Exception e) {
			log.error("MaskUtil failed to load config file");
			return null;
		}
	}

	/**
	 * JSON format message masking
	 * 
	 * @param message
	 * 
	 * @return result
	 */
	public static String maskJson(String message) {
		return maskJson(message, maskRules);
	}

	/**
	 * JSON format message masking
	 * 
	 * @param json
	 * @param maskingMap
	 * 
	 * @return result
	 */
	private static String maskJson(String json, Map<String, Integer> maskingMap) {

		String result = json;

		for (String key : maskingMap.keySet()) {
			Pattern pattern = Pattern.compile(String.format("(^|,|\\{+)(\\s*\"%s\"\\s*:\\s*)([^,\\}]*)", key));
			Matcher matcher = pattern.matcher(result);

			StringBuffer sb = new StringBuffer();
			while (matcher.find()) {
				StringBuilder replace = new StringBuilder(matcher.group(1)).append(matcher.group(2));

				if (maskingMap.get(key) == MASK_TYPE_ALL) replace.append(maskAllDoubleQuoted(matcher.group(3)));
				else if (maskingMap.get(key) == MASK_TYPE_EVENTH) replace.append(maskEventhDoubleQuoted(matcher.group(3)));
				else if (maskingMap.get(key) == MASK_TYPE_FIXED) replace.append(maskFixedDoubleQuoted(maskFixed()));
				else replace.append(maskFixedDoubleQuoted(matcher.group(3)));

				matcher.appendReplacement(sb, replace.toString());
			}
			matcher.appendTail(sb);

			result = sb.toString();
		}

		return result;
	}

	/**
	 * QUERYSTRING format message masking
	 * 
	 * @param message
	 * 
	 * @return result
	 */
	public static String maskQuerystring(String message) {
		return maskQuerystring(message, maskRules);
	}

	/**
	 * QUERYSTRING format message masking
	 * 
	 * @param querystring
	 * @param maskingMap
	 * 
	 * @return result
	 */
	private static String maskQuerystring(String querystring, Map<String, Integer> maskingMap) {

		String result = querystring;

		for (String key : maskingMap.keySet()) {
			Pattern pattern = Pattern.compile(String.format("(^|&|$+)(\\s*%s=)([^&]*)", key));
			Matcher matcher = pattern.matcher(result);

			StringBuffer sb = new StringBuffer();
			while (matcher.find()) {
				StringBuilder replace = new StringBuilder(matcher.group(1)).append(matcher.group(2));

				if (maskingMap.get(key) == MASK_TYPE_ALL) replace.append(maskAllDoubleQuoted(matcher.group(3)));
				else if (maskingMap.get(key) == MASK_TYPE_EVENTH) replace.append(maskEventhDoubleQuoted(matcher.group(3)));
				else if (maskingMap.get(key) == MASK_TYPE_FIXED) replace.append(maskFixedDoubleQuoted(maskFixed()));
				else replace.append(maskFixedDoubleQuoted(matcher.group(3)));

				matcher.appendReplacement(sb, replace.toString());
			}
			matcher.appendTail(sb);

			result = sb.toString();
		}

		return result;
	}

	/**
	 * XML format message masking
	 * 
	 * @param message
	 * 
	 * @return result
	 */
	public static String maskXml(String message) {
		return maskXml(message, maskRules);
	}

	/**
	 * XML format message masking
	 * 
	 * @param xml
	 * @param maskingMap
	 * 
	 * @return result
	 */
	private static String maskXml(String xml, Map<String, Integer> maskingMap) {

		String result = xml;

		for (String key : maskingMap.keySet()) {
			Pattern pattern = Pattern.compile(String.format("(<[^>:]*:?(?i)%s[^>]*?>)([^<]*)(</[^>:]*:?(?i)%s>)", key, key));
			Matcher matcher = pattern.matcher(result);

			StringBuffer sb = new StringBuffer();
			while (matcher.find()) {
				StringBuilder replace = new StringBuilder(matcher.group(1));

				if (maskingMap.get(key) == MASK_TYPE_ALL) replace.append(maskAllDoubleQuoted(matcher.group(2)));
				else if (maskingMap.get(key) == MASK_TYPE_EVENTH) replace.append(maskEventhDoubleQuoted(matcher.group(2)));
				else if (maskingMap.get(key) == MASK_TYPE_FIXED) replace.append(maskFixedDoubleQuoted(maskFixed()));
				else replace.append(maskFixedDoubleQuoted(matcher.group(2)));

				replace.append(matcher.group(3));
				matcher.appendReplacement(sb, replace.toString());
			}
			matcher.appendTail(sb);

			result = sb.toString();
		}

		return result;
	}

	/**
	 * mask all characters except double quotation mark
	 * 
	 * @param toMask
	 * 
	 * @return
	 */
	private static String maskAllDoubleQuoted(String toMask) {
		if (toMask != null) {
			byte[] toMaskBytes = toMask.getBytes();
			byte[] masked = new byte[toMaskBytes.length];

			for (int i = 0; i < toMaskBytes.length; i++) {
				if ((i == 0 || i == toMaskBytes.length - 1) && toMaskBytes[i] == '\"') masked[i] = toMaskBytes[i];
				else masked[i] = '*';
			}

			return new String(masked);
		}

		return toMask;
	}

	/**
	 * mask fixed characters except double quotation mark
	 * 
	 * @param toMask
	 * 
	 * @return
	 */
	private static String maskFixedDoubleQuoted(String toMask) {
		return "\"".concat(toMask).concat("\"");
	}

	/**
	 * mask all eventh characters except double quotation mark
	 * 
	 * @param toMask
	 * 
	 * @return
	 */
	private static String maskEventhDoubleQuoted(String toMask) {
		if (toMask != null) {
			byte[] toMaskBytes = toMask.getBytes();
			byte[] masked = new byte[toMaskBytes.length];

			int offset = 0;
			if (toMaskBytes.length > 0 && toMaskBytes[0] == '\"') offset = 1;

			for (int i = 0; i < toMaskBytes.length; i++) {
				if ((i == 0 || i == toMaskBytes.length - 1) && toMaskBytes[i] == '\"') masked[i] = toMaskBytes[i];
				else if ((i + offset) % 2 == 0) masked[i] = toMaskBytes[i];
				else masked[i] = '*';
			}

			return new String(masked);
		}

		return toMask;
	}

	/**
	 * mask fixed characters mark
	 * 
	 * @param toMask
	 * 
	 * @return
	 */
	private static String maskFixed() {
		return "***";
	}
}