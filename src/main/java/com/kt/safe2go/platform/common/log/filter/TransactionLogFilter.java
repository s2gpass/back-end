package com.kt.safe2go.platform.common.log.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.kt.safe2go.platform.common.log.CommonLog;
import com.kt.safe2go.platform.common.log.ThreadRepository;
import com.kt.safe2go.platform.common.log.vo.TransactionVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@WebFilter(urlPatterns = "*")
public class TransactionLogFilter implements Filter
{
	
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
        throws IOException, ServletException
    {
        long startTime = System.currentTimeMillis();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletRequest currentRequest = (HttpServletRequest) servletRequest;
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(currentRequest);
        // Other details

        // 현재 Session 에서 사용하는 기본 값 설정 후 transactionId 를 받아온다.
        String transactionId = setDefaultSessionValuesAndGetTransactionId(request);

        // API ID extraction
        String api = request.getHeader("x-api-id");
        if (api == null)
        {
            api = "[".concat(request.getMethod()).concat("]").concat(request.getRequestURL().toString());
        }

        // Client IP extraction
        String clientIp = request.getHeader("x-client-ip");
        if (clientIp == null)
        {
            clientIp = request.getRemoteAddr();
        }

        // Api key extraction
        String apiKey = request.getHeader("x-api-key");

        // Create transaction object
        TransactionVo transaction =
            TransactionVo.builder().transactionId(transactionId).api(api).clientIp(clientIp).apiKey(apiKey)
                .startTime(startTime).build();

        log.info(transaction.toString());

        ContentCachingResponseWrapper response =
            new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        // Call Controller
        chain.doFilter(wrappedRequest, response);

        // Write TxLog
        String requestBody = CommonLog.txLogReq(transaction, wrappedRequest);

        long endTime = System.currentTimeMillis();

        // Transaction Update
        transaction.setEndTime(endTime);
        transaction.setStatus(response.getStatus());
        
        // Write TxLog
        //String resultMsg = CommonLog.txLogRes(transaction, response);
        CommonLog.txLogRes(transaction, response);
        
        ThreadRepository.setTransactionThreadLocal(requestBody);
    }

    private String setDefaultSessionValuesAndGetTransactionId(HttpServletRequest request)
    {
        String transactionId = request.getHeader("iptv-tx-id");
        if (StringUtils.isEmpty(transactionId))
        {
            transactionId = UUID.randomUUID().toString();
        }

        String language = request.getHeader("iptv-lang-type");
        if (StringUtils.isEmpty(language))
        {
            language = "ko";
        }
        // Change Thread name to Transaction ID
        Thread.currentThread().setName(transactionId);

   

        return transactionId;
    }

}
