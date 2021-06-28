package com.kt.safe2go.platform.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class BasicIdGenerator {

	// array de 64+2 digitos
	private final static char[] DIGITS66 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', '-', ':', '_', ';' };

	public static String next() {
		UUID u = UUID.randomUUID();
		return toIDString(u.getMostSignificantBits()) + toIDString(u.getLeastSignificantBits());
	}

	private static String toIDString(long i) {
		char[] buf = new char[32];
		int z = 64; // 1 << 6;
		int cp = 32;
		long b = z - 1;
		do {
			buf[--cp] = DIGITS66[(int) (i & b)];
			i >>>= 6;
		} while (i != 0);
		return new String(buf, cp, (32 - cp));
	}

	public static long nextLong() {
		long val = -1;
		do {
			final UUID uid = UUID.randomUUID();
			final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
			buffer.putLong(uid.getLeastSignificantBits());
			buffer.putLong(uid.getMostSignificantBits());
			final BigInteger bi = new BigInteger(buffer.array());
			val = bi.longValue();
		} while (val < 0 || val > 9007199254740991L);
		return val;
	}

	/**
	 * 
	 * left padding
	 * 
	 * 원본 문자열이 null 일 경우 null 반환
	 * 
	 * 원본 문자열 길이가 총 길이보다 길 경우 원본 문자열 반환
	 * 
	 * 
	 * 
	 * @param srcString 원본 문자열
	 * 
	 * @param length    padding 된 총 길이
	 * 
	 * @param padChar   padding 문자
	 * 
	 * @return
	 * 
	 */

	public static String lpad(String srcString, int length, char padChar) {

		try {

			StringBuffer sb = new StringBuffer();

			sb.append("%-");

			sb.append(length - srcString.length());

			sb.append("s");

			return String.format(sb.toString(), "").replaceAll("\\s", String.valueOf(padChar)).concat(srcString);

		} catch (Exception e) {

			return srcString;

		}

	}

	/**
	 * 
	 * right padding
	 * 
	 * 원본 문자열이 null 일 경우 null 반환
	 * 
	 * 원본 문자열 길이가 총 길이보다 길 경우 원본 문자열 반환
	 * 
	 * 
	 * 
	 * @param srcString 원본 문자열
	 * 
	 * @param length    padding 된 총 길이
	 * 
	 * @param padChar   padding 문자
	 * 
	 * @return
	 * 
	 */

	public static String rpad(String srcString, int length, char padChar) {

		try {

			StringBuffer sb = new StringBuffer();

			sb.append("%-");

			sb.append(length - srcString.length());

			sb.append("s");

			return srcString.concat(String.format(sb.toString(), "").replaceAll("\\s", String.valueOf(padChar)));

		} catch (Exception e) {

			return srcString;

		}

	}

}
