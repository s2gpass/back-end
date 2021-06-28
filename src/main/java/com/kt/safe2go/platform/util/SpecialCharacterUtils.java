package com.kt.safe2go.platform.util;

public class SpecialCharacterUtils {
	
	public static String replaceUnicodeCharacters(String string) {
		return string.replace("&", "&amp;")
				.replace(">", "&gt;")
				.replace("<", "&lt;")
				.replace("\"", "&quot;");
	}
}
