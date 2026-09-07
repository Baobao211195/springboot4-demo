package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 0);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long timeTaken = System.currentTimeMillis() - startTime;

        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());

//        logger.info(
//                "FINISHED PROCESSING : METHOD=" + request.getMethod()
//                + "; REQUESTURI=" + request.getRequestURI()
//                + "; REQUEST PAYLOAD=" + requestBody
//                + "; RESPONSE CODE=" + response.getStatus()
//                + "; RESPONSE=" + responseBody
//                + "; TIME TAKEN=" + timeTaken);
        responseWrapper.copyBodyToResponse();
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
