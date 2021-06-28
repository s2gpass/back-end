package com.kt.safe2go.platform.util;

import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * AES 암호화를 위한 지원 클래스
 * Created by SeungJun on 2017-07-19.
 */
@Slf4j
public class AESUtils {

    public static final byte[] KEY_256 = "kt_family_safe_box_gw_aes256_key".getBytes(StandardCharsets.UTF_8);
    public static final byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    // [M] 20200210 공통으로 처리하는 변수는 고정하여 사용 (자원낭비 방지)
    private static final AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    private static final SecretKeySpec newKey = new SecretKeySpec(KEY_256, "AES");

    // [M] 20200210 공통으로 처리하는 변수는 고정하여 사용 (자원낭비 방지)
    private static Cipher encryptCipher = null;
    private static Cipher decryptCipher = null;

    /**
     * <PRE>
     * 1. MethodName: SecurityUtil.encryptAES256()
     * 2. Comment   :
     * </PRE>
     *      [M] 20210110 Cipher class 는 Thread-safe 가 아님으로 synchronized 내역 추가
     * @param plainText Input value that needed to be encrypted.
     * @return encrypt success :: String
     *                 Fail or Exception occur :: null
     */
    public static synchronized String encryptAES256(String plainText) {

        if (plainText == null)
            return null;

        try {
            if (encryptCipher == null) {
                encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                encryptCipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            }
            byte[] textBytes = plainText.getBytes("UTF-8");
            return Base64.encodeBase64String(encryptCipher.doFinal(textBytes));
        } catch (Exception e) {
            log.error("Exception Occur. encryptAES256 ({}) ({})", plainText, e.getMessage());
            return null;
        }
    }

    /**
     * <PRE>
     * 1. MethodName: SecurityUtil.decryptAES256()
     * 2. Comment   :
     * </PRE>
     *      [M] 20210110 Cipher class 는 Thread-safe 가 아님으로 synchronized 내역 추가
     * @param encryptedData Input value that needed to be decrypted.
     * @return decrypt success :: String
     *                 Fail or Exception occur :: null
     */
    public static synchronized String decryptAES256(String encryptedData) {

        if (encryptedData == null)
            return null;

        try {
            if (decryptCipher == null) {
                decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                decryptCipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            }
            byte[] textBytes = Base64.decodeBase64(encryptedData);
            return new String(decryptCipher.doFinal(textBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Exception Occur. decryptAES256 ({}) ({})", encryptedData, e.getMessage());
            return null;
        }

    }
    
    public static void main(String agrs[]) {
    	String en = "QTYLeIqoWVd6McyIJgfrfA==";
    	System.out.println("@test1 = " + decryptAES256(en));
    }
    
}

