package com.ptc.commons.service.impl;

import com.ptc.commons.service.api.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class LoggingServiceImpl implements LoggingService {

    private final static String REQUEST = "REQUEST ";
    private final static String RESPONSE = "RESPONSE ";
    private final static String METHOD = "method=[";
    private final static String PATH = "path=[";
    private final static String HEADERS = "headers=[";
    private final static String PARAMETERS = "parameters=[";
    private final static String BODY = "body=[";
    private final static String RESPONSE_HEADERS = "responseHeaders=[";
    private final static String RESPONSE_BODY = "responseBody=[";
    private final static String CLOSING_BRACKET = "]";



    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameters = buildParametersMap(httpServletRequest);

        stringBuilder.append(REQUEST);
        stringBuilder.append(METHOD).append(httpServletRequest.getMethod()).append(CLOSING_BRACKET);
        stringBuilder.append(PATH).append(httpServletRequest.getRequestURI()).append(CLOSING_BRACKET);
        stringBuilder.append(HEADERS).append(buildHeadersMap(httpServletRequest)).append(CLOSING_BRACKET);

        if (!parameters.isEmpty()) {
            stringBuilder.append(PARAMETERS).append(parameters).append(CLOSING_BRACKET);
        }

        if (body != null) {
            stringBuilder.append(BODY).append(body).append(CLOSING_BRACKET);
        }

        log.info(stringBuilder.toString());
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(RESPONSE);
        stringBuilder.append(METHOD).append(httpServletRequest.getMethod()).append(CLOSING_BRACKET);
        stringBuilder.append(PATH).append(httpServletRequest.getRequestURI()).append(CLOSING_BRACKET);
        stringBuilder.append(RESPONSE_HEADERS).append(buildHeadersMap(httpServletResponse)).append(CLOSING_BRACKET);
        stringBuilder.append(RESPONSE_BODY).append(body).append(CLOSING_BRACKET);

        log.info(stringBuilder.toString());
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}
