package com.kt.safe2go.platform.common.log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.w3c.dom.Node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.kt.safe2go.platform.common.exception.S2GRuntimeException;
import com.kt.safe2go.platform.common.log.vo.CommonLogVo;
import com.kt.safe2go.platform.common.log.vo.TransactionLogVo;
import com.kt.safe2go.platform.common.log.vo.TransactionThreadLocal;
import com.kt.safe2go.platform.common.log.vo.TransactionVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("CommonLog")
public class CommonLog implements InitializingBean {
	private static final Logger S2G_TRANSACTION = LoggerFactory.getLogger("S2G_TRANSACTION");

	@Value("${spring.application.name}")
	private String appName;

	@Value("${spring.profiles.instance}")
	private String appInstance;

	private static String hostName = null;
	private static String ipAddress = null;

	// Whether masking is possible
	private static boolean maskOn = false;
	private static List<String> maskTarget = null; // 마스킹 대상 추가

	// TX LOG record availability
	private static boolean inReqOn = true;
	private static boolean inResOn = true;

	private static Map<String, String> bodyTypeMap = null;

	private static String appId = null;

	@Autowired
	CommonLogVo commonLogContext;

	/**
	 * Initialize transaction, access, and masking settings
	 */
	@Override
	public void afterPropertiesSet() throws S2GRuntimeException {
		try {
			
			hostName = InetAddress.getLocalHost().getHostName();
			ipAddress = InetAddress.getLocalHost().getHostAddress();
			
			inReqOn = (boolean) commonLogContext.getProperties().get("inReq");
			inResOn = (boolean) commonLogContext.getProperties().get("inRes");
			
			maskOn = commonLogContext.getMasking().isEnabled();
			maskTarget = commonLogContext.getMasking().getTarget(); // 마스킹 대상 추가
			appId = appName.concat("-").concat(appInstance);
			
			// Set body type
			Map<String, String> newBodyTypeMap = new ConcurrentHashMap<String, String>();
			Map<String, List<String>> bodyTypeConfig = commonLogContext.getBodyType();
			for (String key : bodyTypeConfig.keySet()) {
				for (String contentType : bodyTypeConfig.get(key)) {
					newBodyTypeMap.put(contentType, key);
				}
			}
			bodyTypeMap = newBodyTypeMap;

			/* Masking rule */
			MaskUtil.getInstance().parseRules(commonLogContext.getMasking().getRule());

		} catch(S2GRuntimeException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} catch (UnknownHostException e) {
			log.error(e.getMessage(), e);
//			throw new Exception(e);
		}
	}

	/**
	 * Transaction log recording: REQUEST
	 * 
	 * @param transaction
	 * @param request
	 */
	public static String txLogReq(TransactionVo transaction, HttpServletRequest request) {
		try {
			if (!inReqOn) return null;

			// Request header extraction
			Map<String, String> headerMap = new LinkedHashMap<String, String>();
			Enumeration<String> headerList = request.getHeaderNames();
			while (headerList.hasMoreElements()) {
				String key = headerList.nextElement();
				headerMap.put(key, request.getHeader(key));
			}

			// Request body type extraction
			String bodyType = null;
			if (request.getMethod().equalsIgnoreCase("GET") || request.getMethod().equalsIgnoreCase("DELETE")) {
				bodyType = "QUERYSTRING";
			} else {
				if (request.getContentType() != null) {
					for (String bodyContentType : bodyTypeMap.keySet()) {
						if (request.getContentType().indexOf(bodyContentType) > -1) {
							bodyType = bodyTypeMap.get(bodyContentType);
						}
					}
				}
			}
			S2G_TRANSACTION.info("txLogREQ {}", request.toString());

			// Request body extraction
			String body = null;

			// In case of GET method, set full text to BODY
			if (request.getMethod().equalsIgnoreCase("GET")) {
				body = request.getQueryString();
			} else {
				   ContentCachingRequestWrapper requestWrapper=(   ContentCachingRequestWrapper)request;
				   body=new String(requestWrapper.getContentAsByteArray());
			//	body = StreamUtils.copyToString(request.getInputStream(), Charset.forName(request.getCharacterEncoding()));
			}

			TransactionLogVo transactionLog = makeTxLogVO("REQ", transaction, bodyType, body, headerMap.toString());

			String requestBody = new ObjectMapper().writeValueAsString(transactionLog);
			
			S2G_TRANSACTION.info(requestBody);
			
			//임시 용도 stb에서 uuid를 나중에 보내 주어서 uuid 를 업데이트 하는 로직 ... 바로 삭제 해야 함.
//			MANAGER.imsiStbUuidChange(headerMap, body, request);
		
			
			return requestBody;
	
    	} catch (Exception e) {
			log.error("Write TxLog(InReq) failure", e);
			return null;
		}
	}
	
	/**
	 * Transaction log recording: REQUEST
	 * 
	 * @param transaction
	 * @param request
	 */
	public static void txLogReq(TransactionVo transaction, HttpRequest request,  String body) {
		try {
			if (!inReqOn) {
				log.info("Write TxLog(InReq) ", inReqOn);
			

				return;
			}

			// Request header extraction
			Map<String, List<String>> headerMap = new LinkedHashMap<String, List<String>>();
			for (Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
				headerMap.put(entry.getKey(), entry.getValue());
			}

			// Request body type extraction
			String bodyType = null;
			if (request.getMethod() == HttpMethod.GET || request.getMethod() == HttpMethod.DELETE) {
				bodyType = "QUERYSTRING";
			} else {
				bodyType = "JSON";
			}

			TransactionLogVo transactionLog = makeTxLogVO("REQ", transaction, bodyType, body, headerMap.toString());

			S2G_TRANSACTION.info(new ObjectMapper().writeValueAsString(transactionLog));
//
//		} catch (BusinesslogicException ex) {
//        	throw ex;
    	} catch (Exception e) {
			log.error("Write TxLog(InReq) failure", e);
		}
	}
	
	/**
	 * Transaction log recording: RESPONSE
	 * 
	 * @param transaction
	 * @param response
	 */
	public static void txLogRes(TransactionVo transaction, ClientHttpResponse response) {
		try {
			if (!inResOn) return;
			
			// Request header extraction
			Map<String, List<String>> headerMap = new LinkedHashMap<String, List<String>>();
			for (Entry<String, List<String>> entry : response.getHeaders().entrySet()) {
				headerMap.put(entry.getKey(), entry.getValue());
			}
			
			String contentType = "";
	        if (response.getHeaders() != null && response.getHeaders().getContentType() != null)
	        {
	            contentType = response.getHeaders().getContentType().toString();
	        }
	        
	        String bodyType = "JSON";

	        StringBuilder inputStringBuilder = new StringBuilder();
	        if (response.getBody() != null && !contentType.contains("image"))
	        {
	            BufferedReader bufferedReader =
	                new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
	            char[] buffer = new char[1024 * 10];

	            int len;
	            while ((len = bufferedReader.read(buffer, 0, buffer.length)) != -1)
	            {
	                inputStringBuilder.append(buffer, 0, len);
	            }
	        }
	        
			String responseBody = inputStringBuilder.toString();

			// Extract response result
			try {
				if (!StringUtils.isEmpty(bodyType) && !StringUtils.isEmpty(responseBody)) {
					if (bodyType.equals("XML")) {
						Node node = convertStringToNode(responseBody);
						transaction.setReturnCode(findNodeInXml(node, "returnCode"));
						if (transaction.getReturnCode().equalsIgnoreCase("F")) {
							transaction.setErrorCode(findNodeInXml(node, "errorCode"));
							transaction.setErrormessage(findNodeInXml(node, "errorMessage"));
						}
					} else if (bodyType.equals("JSON")) {
						transaction.setReturnCode(JsonPath.read(responseBody, "$.returnCode"));
						if (transaction.getReturnCode().equalsIgnoreCase("F")) {
							transaction.setErrorCode(JsonPath.read(responseBody, "$.errorCode"));
							transaction.setErrormessage(JsonPath.read(responseBody, "$.errorMessage"));
						}

					}
				}
	
	    	} catch (PathNotFoundException e) {
				log.warn(e.getMessage());
			}

			TransactionLogVo transactionLog = makeTxLogVO("RES", transaction, bodyType, responseBody, headerMap.toString());

			S2G_TRANSACTION.info(new ObjectMapper().writeValueAsString(transactionLog));
			


    	} catch (Exception e) {
			log.error("Write TxLog(InRes) failure", e);
		}
	}

	/**
	 * Transaction log recording: RESPONSE
	 * 
	 * @param transaction
	 * @param response
	 */
	public static void txLogRes(TransactionVo transaction, ContentCachingResponseWrapper response) {
		try {
			if (!inResOn) return;

			// response header extraction
			Map<String, String> headerMap = new LinkedHashMap<String, String>();
			Collection<String> headerList = response.getHeaderNames();
			for (String key : headerList) {
				headerMap.put(key, response.getHeader(key));
			}

			// Content-Type extraction
			String contentType = response.getContentType();
			if (StringUtils.isEmpty(contentType) && headerMap.containsKey("Content-Type")) {
				contentType = headerMap.get("Content-Type");
			}

			// Character set extraction
			String charSet = response.getCharacterEncoding();
			if (!StringUtils.isEmpty(charSet) && !StringUtils.isEmpty(contentType)) {
				MediaType mediaType = MediaType.parseMediaType(contentType);
				if (mediaType.getCharset() != null) {
					charSet = mediaType.getCharset().toString();
				}
			}

			// Response body type extraction
			String bodyType = null;
			if (contentType != null) {
				for (String bodyContentType : bodyTypeMap.keySet()) {
					if (contentType.indexOf(bodyContentType) > -1) {
						bodyType = bodyTypeMap.get(bodyContentType);
					}
				}
			}

			// Response body extraction
			byte[] responseBodyByteArray = response.getContentAsByteArray();
			response.getResponse().getOutputStream().write(responseBodyByteArray);
			String responseBody = new String(responseBodyByteArray, charSet);

			// Extract response result
			try {
				if (!StringUtils.isEmpty(bodyType) && !StringUtils.isEmpty(responseBody)) {
					if (bodyType.equals("XML")) {
						Node node = convertStringToNode(responseBody);
						transaction.setReturnCode(findNodeInXml(node, "returnCode"));
						if (transaction.getReturnCode().equalsIgnoreCase("F")) {
							transaction.setErrorCode(findNodeInXml(node, "errorCode"));
							transaction.setErrormessage(findNodeInXml(node, "errorMessage"));
						}
					} else if (bodyType.equals("JSON")) {
						transaction.setReturnCode(JsonPath.read(responseBody, "$.returnCode"));
						if (transaction.getReturnCode().equalsIgnoreCase("F")) {
							transaction.setErrorCode(JsonPath.read(responseBody, "$.errorCode"));
							transaction.setErrormessage(JsonPath.read(responseBody, "$.errorMessage"));
						}

					}
				}

	    	} catch (PathNotFoundException e) {
				log.warn(e.getMessage());
			}

			TransactionLogVo transactionLog = makeTxLogVO("RES", transaction, bodyType, responseBody, headerMap.toString());
			String returnInfo = new ObjectMapper().writeValueAsString(transactionLog);
						
			S2G_TRANSACTION.info(returnInfo);				
	
			ThreadRepository.setTransactionThreadLocal(new TransactionThreadLocal(transactionLog,transaction));
			
    	} catch (Exception e) {
			log.error("Write TxLog(InRes) failure", e);
		}
	}

	/**
	 * Transaction log vo setting
	 * 
	 * @param lotType
	 * @param transaction
	 * @param bodyType
	 * @param body
	 * @param header
	 * 
	 * @return
	 */
	private static TransactionLogVo makeTxLogVO(String lotType, TransactionVo transaction, String bodyType, String pBody, String header) {
		String body = pBody;
		TransactionLogVo transactionLog = null;
		try {
			
			// 마스킹 대상 추가
			// If there is no target, the entire API is masked, and if there is a target, only the target API is masked.
			
			if (maskOn  && (
					CollectionUtils.isEmpty(maskTarget)
					|| maskTarget.contains( transaction.getApi().substring(transaction.getApi().lastIndexOf("/") + 1)) )) {
				
				if (!StringUtils.isEmpty(bodyType) && !StringUtils.isEmpty(body)) {
					if (bodyType.equals("JSON")) {
						body = MaskUtil.maskJson(body);
					} else if (bodyType.equals("QUERYSTRING")) {
						body = MaskUtil.maskQuerystring(body);
					} else if (bodyType.equals("XML")) {
						body = MaskUtil.maskXml(body);
					} else {
						body = "";
					}
				} else {
					body = "";
				}
			}

			log.info(lotType + " Header : " + header);
			if( excludeLogApi(transaction.getApi()) ){
				log.info(lotType + " Body : " + body);
			}
			else {
				log.info(lotType + " Body : Log exclude api.");
			}

			long currentTime = transaction.getEndTime() > 0 ? transaction.getEndTime() : System.currentTimeMillis();

			transactionLog = TransactionLogVo.builder().sysId("S2G").applicationId(appId).currentTime(new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN).format(new Date(currentTime))).hostname(hostName).srvrIp(ipAddress)
					.remoteIp(transaction.getClientIp()).classPath("").txId(transaction.getTransactionId()).api(transaction.getApi()).allow(lotType).resultCode(toEmpty(transaction.getReturnCode()))
					.errLevel("").errCode(toEmpty(transaction.getErrorCode())).errMessage(toEmpty(transaction.getErrormessage()))
					.payload(String.valueOf(transaction.getStatus()).concat("|").concat(toEmpty(body))).build();

    	} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return transactionLog;
	}

	/**
	 * Convert to blank
	 * 
	 * @param str
	 * 
	 * @return
	 */
	private static String toEmpty(String str) {
		return str == null ? "" : str;
	}

	/**
	 * Convert string to XML format
	 * 
	 * @param str
	 * 
	 * @return node
	 */
	public static Node convertStringToNode(String str) {
		Node node = null;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
						
			documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
			documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			documentBuilderFactory.setExpandEntityReferences(false);
			documentBuilderFactory.setXIncludeAware(false);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			node = documentBuilder.parse(new ByteArrayInputStream(str.getBytes("UTF-8")));
	
    	} catch (Exception e) {
			return null;
		}
		return node;
	}

	/**
	 * XML data extraction
	 * 
	 * @param node
	 * @param key
	 * 
	 * @return value
	 */
	public static String findNodeInXml(Node node, String key) {
		String value = "";
		
		if(node == null) {
			return value;
		}
		
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node childNode = node.getChildNodes().item(i);
			if (childNode != null && childNode.hasChildNodes()) {
				if (key.equals(childNode.getNodeName())) {
					value = childNode.getTextContent();
					break;
				} else {
					value = findNodeInXml(childNode, key);
					if (!StringUtils.isEmpty(value)) {
						break;
					}
				}
			}
		}
		return value;
	}
	
	public static boolean excludeLogApi(String api) {
		if(api == null) {
			return false;
		}
		return true;
	}
}
