
package com.kt.safe2go.platform.common.msg;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Constants {

    public static final Marker MARKER = MarkerFactory.getMarker("LOGSTASH");

	public static final String NAMESPACE_DEFAULT 				= "com.kt.safe2go.platform";
	public static final String AUTHORIZATION 					= "Authorization";
	public static final int DEFAULT_EXPIRED_DAY 				= 7;

	public static final String DEL_YN                       	= "delYn";
	public static final String INTERFACE_YN                   	= "ifYn";
	public static final String STR_YES							= "Y";
	public static final String STR_NO                      		= "N";
	public static final String STR_OK                       	= "OK";
	public static final String STR_NOK                      	= "NOK";
	public static final String STR_DUP                      	= "DUP";
	public static final String CONTROL_TYPE						= "contlType";
	public static final String STTUS_TYPE						= "sttusType";

	public static final String PROPERTY_GROUP_TEMP_CFG 			= "temp.cfg";

	public static final String PARAM_INFO						= "paramInfo";
	public static final String DEVICE							= "device";
	public static final String SCENE							= "scene";
	public static final String DEVICES							= "devices";
	public static final String TRACE_ID							= "traceId";
	public static final String LOG_TYPE							= "logType";
	public static final String STATISTIC_TYPE					= "statisticType";
	public static final String CATEGORY							= "category";
	public static final String KEY								= "key";
	public static final String VALUE							= "value";
	public static final String LOG_TYPE_LOG						= "log";
	public static final String LOG_TYPE_STATS					= "statistic";
	public static final String LOG_TYPE_IF						= "interface";
	public static final String SERVER							= "server";
	public static final String EXEC_TIME						= "execTime";
	public static final String SERVER_CODE						= "serverCode";

	/** 자체로그인 관련 키  */
	public static final String B2B_CONNECT_CHANNEL				= "connChannel";
	public static final String B2B_CONNECT_LOCAL				= "L";
	public static final String B2B_CONNECT_GBAS				= "G";
	public static final String B2B_SELF_CERTIFICATION_YN				= "selfCertificationYn";
    public static final String COUNT				= "cnt";


	/** language 관련 키 */
	public static final String LANG_KO                      	= "ko";
	public static final String LANG_EN                      	= "en";

	public static final String LANG_ID 							= "langId";
	public static final String LANG_TYPE 						= "langType";
	public static final String LANG_TEXT 						= "langText";



}