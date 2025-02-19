package com.be.custom.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StatsFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsFilter.class);

    @Value("${log-util.min-stat}")
    private long MIN_STAT;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        Instant start = Instant.now();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            if (time > MIN_STAT && request.getRequestURI().startsWith("/api")) {
                LOGGER.error("This request over MIN_STAT: {} - {} ms ", request.getRequestURI(), time);
            }
        }
    }

    @Override
    public void destroy() {
        //do nothing
    }
}
