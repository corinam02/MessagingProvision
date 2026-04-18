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
import jakarta.servlet.Filter;
import java.util.logging.LogRecord;

@Component
@Order(1)
public class CorrelationFilterId implements Filter {
    public static final String HEADER = "X-Correlation-Id";
    public static final String MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String correlationId = req.getHeader(HEADER);
        if(correlationId == null || correlationId.isBlank()){
            correlationId = UUID.randomUUID().toString();
        }


        MDC.put(MDC_KEY, correlationId);
        res.setHeader(HEADER, correlationId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

}
