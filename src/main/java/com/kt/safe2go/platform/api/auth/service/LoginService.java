package com.kt.safe2go.platform.api.auth.service;

import java.util.HashMap;
import java.util.Map;

import com.kt.safe2go.platform.api.auth.dao.LoginMapper;
import com.kt.safe2go.platform.api.auth.model.MemberVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    LoginMapper loginMapper;

    public String selectAuthIpCheck(String userIp) {
        return loginMapper.selectAuthIpCheck(userIp);
    };

    public int insertAuth(String recvCtn, String smsKey, String encAesIp, String reqType) {
        return loginMapper.insertAuth(recvCtn, smsKey, encAesIp, reqType);
    };

    public int authSMSChk(String recvCtn, String sendSMS, String reqType) {
        return loginMapper.authSMSChk(recvCtn, sendSMS, reqType);
    };
    
    public int authSMSChkSuccess(String recvCtn, String sendSMS, String reqType) {
        return loginMapper.authSMSChkSuccess(recvCtn, sendSMS, reqType);
    };

    public int authSMSChkFail(String recvCtn, String sendSMS, String reqType) {
        return loginMapper.authSMSChkFail(recvCtn, sendSMS, reqType);
    };

    public int insertSign(MemberVO onmMemVo) {
        return loginMapper.insertSign(onmMemVo);
    };

    public MemberVO selectOnmUser(String phoneNum) {
        return loginMapper.selectOnmUser(phoneNum);
    };

    public int loginCntHist(String phoneNum) {
        return loginMapper.loginCntHist(phoneNum);
    };

    public int insertIpCngReq(String onmId, String updIp, String oldIp) {
        return loginMapper.insertIpCngReq(onmId, updIp, oldIp);
    };

    public Map<String,String> loginIpChk(String phoneNum) {
        return loginMapper.loginIpChk(phoneNum);
    }

	public int insertLoginHist(String onmId, String company, String department, String accessIp) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("onmId", onmId);
		param.put("compony", company);
		param.put("department", department);
		param.put("accessIp", accessIp);
		
		return loginMapper.insertLoginHist(param);
	}

	public int updateLoginHist(String onmId) {
		return loginMapper.updateLoginHist(onmId);
	}

	public int updateUserName(String onmId, String userNm) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("onmId", onmId);
		param.put("userNm", userNm);
		
		return loginMapper.updateUserName(param);
	};
}
