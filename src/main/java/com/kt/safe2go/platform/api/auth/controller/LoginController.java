package com.kt.safe2go.platform.api.auth.controller;


import javax.servlet.http.HttpSession;

import com.kt.safe2go.platform.api.auth.model.MemberVO;
import com.kt.safe2go.platform.api.auth.service.LoginService;

import com.kt.safe2go.platform.common.exception.S2GRuntimeException;
import com.kt.safe2go.platform.common.msg.StatusEnum;
import com.kt.safe2go.platform.common.msg.StatusMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/service")
public class LoginController {

	
    @Autowired
    private LoginService loginService;

	
	@Value("${spring.profiles.active}")
	private String runningMode;
    
    
    @RequestMapping(value = "/login", method = { RequestMethod.GET })
    public ResponseEntity<StatusMsg> login(@RequestBody MemberVO memberVO, HttpSession session) {
    	StatusMsg statusMsg = new StatusMsg();
    	HttpHeaders headers = new HttpHeaders();
        log.info("==== sign login page success ====");

        return new ResponseEntity<>(statusMsg, headers, HttpStatus.OK);
    }

  
     @RequestMapping(value = "/signUp", method = { RequestMethod.POST })
     public ResponseEntity<StatusMsg> signUser(@RequestBody MemberVO memberVO, HttpSession session) throws S2GRuntimeException{

         log.info("==== sginUser : {}", memberVO);

         int res = 0;
         HttpHeaders headers = new HttpHeaders();
         StatusMsg statusMsg = new StatusMsg();
   
         res = loginService.insertSign(memberVO);
     
         if (res == 0) {
             statusMsg.setReturnCode(StatusEnum.RET_FAIL.getMessage());
             statusMsg.setErrorCode(StatusEnum.RET_FAIL.getCode());
             statusMsg.setErrorMessage("승인요청 실패");
             statusMsg.setData(res);

             return new ResponseEntity<>(statusMsg, HttpStatus.OK);
         }

         MemberVO loginVo = (MemberVO) session.getAttribute("memberVO");

         statusMsg.setReturnCode(StatusEnum.RET_SUCC.getMessage());
         statusMsg.setData(res);
                  
         
         log.info("==== sginUser : {}", statusMsg);
         return new ResponseEntity<>(statusMsg, HttpStatus.OK);
     }
            
}
