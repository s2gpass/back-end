package com.kt.safe2go.platform.api.auth.dao;

import java.util.Map;


import com.kt.safe2go.platform.api.auth.model.MemberVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {

    String selectAuthIpCheck(@Param("USER_IP") String userIp);

	int selectAuthSMS(@Param("RECV_CTN") String recvCtn);

    int insertAuth(@Param("RECV_CTN") String recvCtn, @Param("SMS_KEY") String smsKey,
            @Param("USER_IP") String encAesIp, @Param("REQ_TYPE") String reqType);
    
    int authSMSChk(@Param("PHONE_NUM") String recvCtn, @Param("SEND_SMS") String sendSMS, @Param("REQ_TYPE") String reqType);

    int authSMSChkSuccess(@Param("PHONE_NUM") String recvCtn, @Param("SEND_SMS") String sendSMS, @Param("REQ_TYPE") String reqType);

    int authSMSChkFail(@Param("PHONE_NUM") String recvCtn, @Param("SEND_SMS") String sendSMS, @Param("REQ_TYPE") String reqType);

    int insertSign(MemberVO onmMemVo);

    MemberVO selectOnmUser(@Param("PHONE_NUM") String phoneNum);

    int loginCntHist(@Param("PHONE_NUM") String phoneNum);

    int insertIpCngReq(@Param("ONM_ID") String onmId, @Param("UPD_IP") String updIp, @Param("OLD_IP") String oldIp);

    Map<String,String> loginIpChk(@Param("PHONE_NUM") String phoneNum);

	int insertLoginHist(Map<String, Object> param);

	int updateLoginHist(String onmId);

	int updateUserName(Map<String, Object> param);
    
}
