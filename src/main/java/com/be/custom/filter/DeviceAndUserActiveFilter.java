package com.be.custom.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
public class DeviceAndUserActiveFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String memberCode = httpServletRequest.getHeader("memberCode");
        String deviceId = httpServletRequest.getHeader("deviceId");
        String appIdString = httpServletRequest.getHeader("appId");
        String typeOs = httpServletRequest.getHeader("typeOs");
        chain.doFilter(request, response);
    }

    private void saveUserActive(Long appId, Long companyId, String memberCode, String typeOS) {
        if (memberCode == null) {
            return;
        }
    }

    private void saveDeviceActive(Long appId, Long companyId, String deviceIdStr, String typeOS) {
        if (deviceIdStr == null) {
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
