package com.kt.safe2go.platform.util;

public class Constants {
	
	public static final String MSG_OK 		= "OK";
	public static final String MSG_NG 		= "NG";
	
	
	public static final int RESULT_NOTBLOCK 		= 0; //허용		
	public static final int RESULT_BLOCK 			= 1; //차단	
	
	
	public static final int RETCD_OK 		= 0;
	public static final int RETCD_NG 		= -1;
	public static final int RETCD_NG_DB		= -999;
	
	
	
	public static final String MSG_NG_404 	= "Not Found, Bad Request.";
	public static final String MSG_NG_405 	= "Request method 'GET' not supported";
	public static final String MSG_NG_400 	= "Bad Request";
	public static final String MSG_NG_DB 	= "DB에러. 입력 파라미터를 확인하세요.";
	public static final String MSG_CONSTRAINT_VIOLATION
											= "위반항목 : %s, 위반내용 : %s";	
		
	
	public static final String HARMFUL_SITE_NODE_NAME_SURL 		= "surl";
	public static final String HARMFUL_SITE_NODE_NAME_PORT 		= "port";
	public static final String HARMFUL_SITE_NODE_NAME_PATH 		= "path";
	public static final String HARMFUL_SITE_NODE_NAME_PARAM 	= "param";
	public static final String HARMFUL_SITE_NODE_NAME_LEVEL 	= "level";
	public static final String HARMFUL_SITE_NODE_NAME_SOURCE 	= "source";
	public static final String HARMFUL_SITE_NODE_NAME_STATUS 	= "status";
	
	
	public static final String BLANK_URL		 	= "/";
	
	public static final String REDIS_KEY_FULLURL 	= "BROADCAST_FULLURL";
	public static final String REDIS_KEY_PATHURL 	= "BROADCAST_PATHURL";
	public static final String REDIS_KEY_DOMAINURL	= "BROADCAST_DOMAINURL";
	public static final String REDIS_VALUE_BLOCK 	= "0";
	
	
	public static final String HARMFUL_APP_NODE_NAME_SPACKAGENAME	= "spackagename";
	public static final String HARMFUL_APP_NODE_NAME_STATUS 		= "status";
	public static final String HARMFUL_APP_NODE_VALUE_STATUS_INSERT= "I";
	public static final String HARMFUL_APP_NODE_VALUE_STATUS_DELETE= "D";
	
	public static final String REDIS_KEY_HARMFULAPP 	= "BROADCAST_APP";
	public static final String REDIS_KEY_HARMFULLURL 	= "BROADCAST_FULLURL";		
		
}
