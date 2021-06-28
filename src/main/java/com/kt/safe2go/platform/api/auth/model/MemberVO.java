package com.kt.safe2go.platform.api.auth.model;

import lombok.Data;
/**
 * ONM 가입자 정보
 */
@Data
public class MemberVO {
	private String rnum;           // NO
    private String onmId;          // 가입자 ID
    private String onmIdMask;      // 가입자 ID (masking)
    private String userNm;         // 이름
    private String userNmMask;     // 이름 (masking)
    private String company;        // 소속회사
    private String department;     // 부서
    private String email;          // 이메일
    private String status;         // 가입상태
    private String acceptId;       // 승인자 ID
    private String mobileNum;      // 사용자 전화번호
    private String regDate;        // 등록일
    private String updDate;        // 갱신일
    private String roleName;       // 권한 이름
    private String accessIp;       // 접속아이피주소
    private int loginCnt;          // 로그인 실패 카운트
    private String loginDate;      // 마지막 로그인 일시
    
    // 권한 변경 요청 컬럼
    private int seq;        
    private String updType;        
    private String oldDesc;
    private String updDesc;
    private String accessRegDate;
    private String accessUpdDate;
    private String accessUpdId;
    private String useYn;
    
    //test
    private String test;
    private String aa;
}
