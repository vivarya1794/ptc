package com.ptc.commons.interceptors;

import com.ptc.commons.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The interceptor as the name suggests will intercept all the requests except
 * for the ones configured as an exclusion. This class is responsible for triggering the
 * authentication and the authorization.
 */
@Slf4j
@Component
public class ApiRequestInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        setInteractionData(request);
        log.info("caller:{}, host:{}, httpMethod:{}, uri:{}, parameters:{}", Interaction.getCaller(), request.getHeader("host"), request.getMethod(), request.getRequestURI(), request.getQueryString());
        return true;
    }

    private void setInteractionData(final HttpServletRequest request) {
        Interaction.begin();
        Interaction.markStartTime();
        Interaction.requestId(request.getHeader(AppConstants.HEADER_X_REQUEST_ID));
        Interaction.caller(StringUtils.defaultIfEmpty(request.getHeader(AppConstants.HEADER_X_CLIENT_ID), AppConstants.CALLER_UNKNOWN));
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
        log.info("user:{}, uri:{}", Interaction.getUser(), request.getRequestURI());
        Interaction.end();
    }

}