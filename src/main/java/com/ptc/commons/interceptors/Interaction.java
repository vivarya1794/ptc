package com.ptc.commons.interceptors;

import com.ptc.commons.utils.AppConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

public final class Interaction {

    public static final String DEFAULT_USER = "SYSTEM";

    private Interaction() {
    }

    public static void begin() {
        MDC.put(AppConstants.HEADER_X_USER_ID, DEFAULT_USER);
    }

    public static void end() {
        MDC.clear();
    }

    public static void markStartTime() {
        MDC.put("startTime", System.currentTimeMillis() + "");
    }

    public static long getStartTime() {
        return Long.parseLong(MDC.get("startTime"));
    }

    public static void requestId(final String requestId) {
        if (StringUtils.isEmpty(requestId)) {
            MDC.put(AppConstants.HEADER_X_REQUEST_ID, UUID.randomUUID().toString());
            return;
        }
        MDC.put(AppConstants.HEADER_X_REQUEST_ID, requestId);
    }

    public static String getRequestId() {
        final String requestId = MDC.get(AppConstants.HEADER_X_REQUEST_ID);
        if (StringUtils.isEmpty(requestId)) {
            return UUID.randomUUID().toString();
        }
        return requestId;
    }

    public static void user(final String user) {
        MDC.put(AppConstants.HEADER_X_USER_ID, user);
    }

    public static String getUser() {
        return MDC.get(AppConstants.HEADER_X_USER_ID);
    }

    public static void caller(final String caller) {
        MDC.put(AppConstants.HEADER_X_CLIENT_ID, caller);
    }

    public static String getCaller() {
        return MDC.get(AppConstants.HEADER_X_CLIENT_ID);
    }
}
