package com.kt.safe2go.platform.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;


public class Util {
	
	/**
	 * 현재 시간 TIMESTAMP
	 * 
	 * @return long
	 */
	public static long getCurrentTimeStamp() {
		 return Calendar.getInstance().getTime().getTime();
	}
	
	/**
	 * SMS인증키 생성 
	 * 
	 * @param input 길이
	 */
    public static String makeSMSKey(int length) {
        Random rand = new Random(System.currentTimeMillis());

        String authKey = "";
        for(int i = 0; i < length; i++)
            authKey += String.valueOf(rand.nextInt(10));

        return authKey;
    }
    
    /**
     * base64 encode, decode 
     */
    public static String base64Encoding(String text) {
        byte[] encoded = Base64.encodeBase64(text.getBytes());
        
        return new String(encoded);
    }
    
    /**
     * base64 encode, decode 
     *
     */
    public static String base64Decoding(String text) {
        /* base64 decoding */
        byte[] decoded = Base64.decodeBase64(text);
        
        return new String(decoded);
    }
    
    /**
     * @param inputData
     * @return
     */
    public static String getHashedValue(String inputData) {
        String sResp = null;
        String sha1 = "";
        try {
            byte byteHash[];
        	
        	sha1 = org.apache.commons.codec.digest.DigestUtils.sha1Hex( inputData );

        } catch (Exception e) {
            System.err.println("getHashedValue failed: " + e.getMessage());
            return null;
        }
        return sha1;
    }
    
    /**
     * tracekey생성
     * @param rn
     * @return tracekey
     */
    public static String createSessionKey(Random rn)
    {
        long randomNumber1 = rn.nextInt((99999999 - 10000000) + 1) + 10000000;
        long randomNumber2 = rn.nextInt((99999999 - 10000000) + 1) + 10000000;

        StringBuilder bl = new StringBuilder();
        char ch;
        for (int i = 0; i < 4; i++)
        {
        	double dVal = Math.floor(26 * rn.nextDouble() + 65);
        	int iVal = (int)dVal;
            ch = (char)iVal;
            bl.append(ch);
        }

        return bl.toString().toUpperCase() + randomNumber1 + randomNumber2;
    }


//	public static String ObjectToString(Object _value)
//    {
//        if (_value == null) return "";
//        try
//        {
//            return _value.toString();
//        } catch (Exception e) {
//            return "";
//        }
//    }
	
	/// <summary>
    /// DB에 넣은 문자열 날짜 14자리
    /// </summary>
    /// <returns></returns>
	
	private static final SimpleDateFormat FORMAT_yyyyMMddHH24mmssSS = new SimpleDateFormat("yyyyMMddHH24mmssSS");
	private static boolean add;
	
    public static String GetTimeString() {
    	Date date = new Date();
    	return FORMAT_yyyyMMddHH24mmssSS.format(date);
    }
    
    // 지정된 범위의 정수 1개를 램덤하게 반환하는 메서드
    // n1 은 "하한값", n2 는 상한값
    public static long randomRange(long n1, long n2) {
      return (long) (Math.random() * (n2 - n1 + 1)) + n1;
    }
    
    /// <summary>
    /// 0이 붙은 3-4-4 형태를 3-3-4로 변환
    /// </summary>
    /// <param name="src"></param>
    /// <returns></returns>
    public static String ReversePhoneNumber(String src)
    {
    	if (src != null && src.length() == 11)
        {

        	String _phone = src.substring(0, 3);

            if (src.substring(3, 4).equals("0"))
            {
                _phone += src.substring(4, 7) + src.substring(7, 11);
            }
            else
            {
                _phone = src.substring(0, 3) + src.substring(3, 7) + src.substring(7, 11);
            }
            return _phone;
        }

        return src;
    }
    
    ///<summary>
    ///폰번호 PRINT 
    ///</summary>
    ///<param name="Pno">폰번호</param>///
    ///<returns>String strwrite</returns> 
    public static String PhoneCut(String Pno)
    {
    	String strwrite = "";
        Pno = Util.ReversePhoneNumber(Pno);

        if (Pno.length() == 11)
        {
            strwrite = Pno.substring(0, 3) + "-" + Pno.substring(3, 7) + "-" + Pno.substring(7, 11);
        }
        else if (Pno.length() == 10)
        {
            strwrite = Pno.substring(0, 3) + "-" + Pno.substring(3, 6) + "-" + Pno.substring(6, 10);
        }

        return strwrite;
    }
    
    ///<summary>
    ///폰번호 PRINT 
    ///</summary>
    ///<param name="Pno">폰번호</param>///
    ///<returns>String strwrite</returns> 
    public static String PhoneCuts(String Pno)
    {
    	String strwrite = "";

        if (Pno.length() == 12)
        {
            strwrite = Pno.substring(1, 12);
            
        	String _phone = strwrite.substring(0, 3);
        	if (strwrite.substring(3, 4).equals("0"))
            {
                _phone += strwrite.substring(4, 7) + strwrite.substring(7, 11);
            }
            else
            {
                _phone = strwrite.substring(0, 3) + strwrite.substring(3, 7) + strwrite.substring(7, 11);
            }
            return _phone;
            
        } else if(Pno.length() == 11) {
        	
        	if(Pno.substring(0, 2).equals("00")) { // 지역번호
        		strwrite = Pno.substring(1, 11);
        	} else {
        		
        		if(Pno.substring(3, 4).equals("0")) { // 국번
        			String _phone = Pno.substring(0, 3);
        			_phone += strwrite.substring(4, 7) + strwrite.substring(7, 11);
        			return _phone;
        		} else {
        			strwrite = Pno;
        		}
        	}
        	
            
        }

        return strwrite;
    }
    
    /// <summary>
    /// Null값을 빈문자열로 치환 
    /// </summary>
    /// <param name="input">문자열</param>
    /// <returns></returns>
    public static String ConvertNullToEmptyString(String input)
    {
        return (input == null ? "" : input);
    }
    
    /**
     * 바이트를 체크한다. 기준보다 크면 false, 작거나 같으면 true
     * 
     * @param txt 체크할 text
     * @param standardByte 기준 바이트 수
     * @return 
     */
    public static boolean byteCheck(String txt, int standardByte) {
        if (StringUtils.isEmpty(txt)) { return true; }
 
        // 바이트 체크 (영문 1, 한글 2, 특문 1)
        int en = 0;
        int ko = 0;
        int etc = 0;
 
        char[] txtChar = txt.toCharArray();
        for (int j = 0; j < txtChar.length; j++) {
            if (txtChar[j] >= 'A' && txtChar[j] <= 'z') {
                en++;
            } else if (txtChar[j] >= '\uAC00' && txtChar[j] <= '\uD7A3') {
                ko++;
                ko++;
            } else {
                etc++;
            }
        }
 
        int txtByte = en + ko + etc;
        if (txtByte > standardByte) {
            return false;
        } else {
            return true;
        }
 
    }
    
    /**
     * lamp사용
     *
     */
    public static String unicodeConvert(String str) {
		StringBuilder sb = new StringBuilder();
		char ch;
		int len = str.length();
		for (int i = 0; i < len; i++) {
			ch = str.charAt(i);
			if (ch == '\\' && str.charAt(i + 1) == 'u') {
				sb.append((char) Integer.parseInt(str.substring(i + 2, i + 6),
						16));
				i += 5;
				continue;
			}
			sb.append(ch);
		}
		return sb.toString();
	}
    
    /**
     * <pre>
     * 1. 개요            : Client IP 조회
     * 2. 처리내용       : 
     * </pre>
     * @Method Name : GetConnIP
     * @return type : String
     * @throws      : 
     * @desc        :
     */
    public static String GetConnIP(HttpServletRequest request)
    {
    	String ip = request.getHeader("X-Forwarded-For");
    	 
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
	/// int로 null 체크
	public static int ConvertIntValue(String data) {
		return Integer.valueOf((data == null || data.equals("")) ? "0" : data);
	}
	
	// 고유키 생성
	public static String createUUID(String prefix, String suffix) {
    	
    	String makeStr = prefix;
    	
    	String id = BasicIdGenerator.rpad(BasicIdGenerator.next(), 30, '0');
    	
    	makeStr = makeStr + "_" + id;
    	
    	int makeStrLen = makeStr.length();
    	int suffixLen = suffix.length();
    	String returnStr = makeStr.substring(0, makeStrLen-suffixLen-1).concat("_").concat(suffix);
    	
		return returnStr;
    	
    }
	
	public static String getMime(String extension) {

        switch (extension.toLowerCase()) {
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "json":
                return "applicaion/json";
            default:
                return "application/octet-stream";
        }
    }
	
	/// XSS 방지 처리
	public static String CheckParam(String input) {
		if (input == null) {
			input = "";
		} else {
			input = input.replace("<", "&lt;");
			input = input.replace(">", "&gt;");
			input = input.replace("'", "&#39;");
			input = input.replace("\"", "&#34;");
			// input = input.replace("%", "&#37;");
			// input = input.replace(";", "&#59;");
			input = input.replace("(", "&#40;");
			input = input.replace(")", "&#41;");
			// input = input.replace("&", "&#38;");
			// input = input.replace("+", "&#43;");

		}
		return input;
	}

	/// XSS 방지 원상 복원
	public static String ReverseParam(String input) {
		if (input == null) {
			input = "";
		} else {
			input = input.replace("&lt;", "<");
			input = input.replace("&gt;", ">");
			input = input.replace("&#39", "'");
			input = input.replace("&#34;", "\"");
			// input = input.replace("&#37;", "%");
			// input = input.replace("&#59;", ";");
			input = input.replace("&#40;", "(");
			input = input.replace("&#41;", ")");
			// input = input.replace("&#38;", "&");
			// input = input.replace("&#43;", "+");

		}
		return input;
	}
	
	// MD5 생성
	public static String generateMD5(String str) {
		String MD5 = ""; 

		try{

			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(str.getBytes()); 
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}

			MD5 = sb.toString().toUpperCase();

		}catch(NoSuchAlgorithmException e){

//			e.printStackTrace();
			MD5 = null; 

		}

		return MD5;
	}

    /**
     * 로그를 잘 직기 위함
     * @param value
     * @return
     */
	public static String subStringForLog(String value, int length) {
        if (value == null)
            return null;
        if (value.length() > length)
            return value.substring(0, length);
        return value;
    }
	
	/**
	 * <PRE>
	 * 1. MethodName: Utils.checkNull()
	 * 2. Comment	: 
	 * </PRE>
	 * 
	 * @param str
	 * @return String
	 */
	public static String checkNull(String str) {
		if (str == null)
			return "";
		else
			return str;
	}
	
	public static String getMaskedName(String name) {
	    String maskedName = "";     // 마스킹 이름
	    String firstName = "";      // 성
	    String middleName = "";     // 이름 중간
	    String lastName = "";       //이름 끝
	    int lastNameStartPoint;     // 이름 시작 포인터
	    
	    if(!name.equals("") || name != null){
	        if(name.length() > 1){
	            firstName = name.substring(0, 1);
	            lastNameStartPoint = name.indexOf(firstName);
	            
	            if(name.trim().length() > 2){
	                middleName = name.substring(lastNameStartPoint + 1, name.trim().length()-1);
	                lastName = name.substring(lastNameStartPoint + (name.trim().length() - 1), name.trim().length());
	            }else{
	                middleName = name.substring(lastNameStartPoint + 1, name.trim().length());
	            }
	            
	            String makers = "";
	            for(int i = 0; i < middleName.length(); i++){
	                makers += "*";
	            }
	            
	            lastName = middleName.replace(middleName, makers) + lastName;
	            maskedName = firstName + lastName;
	        }else{
	            maskedName = name;
	        }
	    }
	    
	    return maskedName;
	}
	
	public static String getMaskedPhone(String phoneNumber) {

        if(phoneNumber == null || phoneNumber.equals("")){
            return null;
        }
	    String replaceString = phoneNumber;
	    String mobilePattern = "^(\\d{3})-?(\\d{3,4})-?(\\d{4})$";
	    String maskPhone = null;
	    
	    Matcher matcher = Pattern.compile(mobilePattern).matcher(phoneNumber);

	    if(matcher.matches()) {
	        replaceString = "";

	        boolean isHyphen = false;
	        if(phoneNumber.indexOf("-") > -1) {
	            isHyphen = true;
	        }
	        
	          String[] phoneArr = phoneNumber.split("");
	          phoneArr[5] = "*";
	          phoneArr[6] = "*";
	          phoneArr[7] = "*";
	          phoneArr[8] = "*";
	          
	          maskPhone = String.join("", phoneArr);
	    }
	    return maskPhone;
	}
	
	public static String getMaskedEmail(String email) {
		String replaceString = email;
		
		Matcher matcher = Pattern.compile("^(..)(.*)([@]{1})(.*)$").matcher(email);
		
		if(matcher.matches()) {
			replaceString = "";
			
			for(int i=1;i<=matcher.groupCount();i++) {
				String replaceTarget = matcher.group(i);
				if(i == 2) {
					char[] c = new char[replaceTarget.length()];
					Arrays.fill(c, '*');
					
					replaceString = replaceString + String.valueOf(c);
				} else {
					replaceString = replaceString + replaceTarget;
				}
			}
			
		}
		
		return replaceString;
	}

}
