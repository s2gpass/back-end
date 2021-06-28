package com.kt.safe2go.platform.util;

import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class TransactionID {
	public static String create() {
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS")
				+ RandomUtils.nextInt(1000, 9999);
	}
}
