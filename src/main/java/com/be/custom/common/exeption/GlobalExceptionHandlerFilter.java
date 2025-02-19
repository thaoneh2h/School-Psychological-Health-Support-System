package com.be.custom.common.exeption;

import com.be.base.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandlerFilter implements Filter {

    private final EmailService emailService;

    @Value("${email.is-on-send-mail}")
    public boolean isOnSendMailError;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (isOnSendMailError) {
            try {
                chain.doFilter(request, response);
            } catch (Exception ex) {
                log.error("request is: {}", httpServletRequest.getRequestURI());
                if (ex instanceof ClientAbortException) {
                    log.error("Client abort request!!");
                } else if (ex.getMessage().contains("Unexpected EOF read on the socket") || ex.getMessage().contains("Processing of multipart/form-data request failed")) {
                    log.error("Request upload file had been closed by client !!!");
                } else {
                    emailService.sendErrorReport(ExceptionUtils.getStackTrace(ex), httpServletRequest);
                    throw ex;
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) {

    }

}
