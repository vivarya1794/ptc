package com.ptc.commons.config;

import com.ptc.commons.interceptors.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * Contains all the Application's security configurations
 */
public class WebConfig implements WebMvcConfigurer {

    LogInterceptor logInterceptor;


    @Autowired
    public WebConfig(final LogInterceptor logInterceptor) {
        this.logInterceptor = logInterceptor;
    }

    /**
     * Registers the interceptor
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
    }

}