package com.ptc.commons.utils;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;

/**
 * Contains the utility methods used throughout the project
 */
@Slf4j
public final class CommonUtil {

    private CommonUtil() {
    }

    /**
     * Exposes a comma (,) seperated list of headers to be used in the CORS.
     * @param response HttpServletResponse object
     */
    public static void exposeRequiredHeaders(final HttpServletResponse response) {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, Joiner.on(",").join(HttpHeaders.AUTHORIZATION, AppConstants.USER_TOKEN_HEADER_NAME));
    }
}