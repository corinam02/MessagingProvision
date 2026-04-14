package com.example.messageprovisioning.provisioning.model.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

@Component
@Order(1)
public class CorrelationFilterId implements Filter {
    public static final String HEADER = "X-Correlation-Id";
    public static final String MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, SerialException, ServletException {
        HttpServletRequest req =(HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) res;

        String correlaionId = req.getHeader(HEADER);
        if(correlaionId == null || correlaionId.isBlank()){
            correlaionId = UUID.randomUUID().toString();
        }

        MDC.put(MDC_KEY, correlaionId);
        res.setHeader(HEADER, correlaionId);

        try {
            chain.doFilter(request,response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

}
