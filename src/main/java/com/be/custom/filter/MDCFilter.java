package com.be.custom.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

@Component
public class MDCFilter implements Filter {

    public void init(FilterConfig args) throws ServletException {
        //do nothing
    }

    public void destroy() {
        //do nothing
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        MDC.put("id", UUID.randomUUID().toString());
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
