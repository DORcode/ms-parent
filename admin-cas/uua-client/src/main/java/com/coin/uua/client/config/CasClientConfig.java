package com.coin.uua.client.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;

import javax.servlet.FilterConfig;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CasClientConfig
 * @Description: Cas客户端配置类
 * @Author kh
 * @Date 2020-07-30 19:25
 * @Version V1.0
 **/
@Configuration
public class CasClientConfig {

    @Value("${cas.server.url.prefix}")
    private String casServerUrlPrefix;

    @Value("${cas.server.url.login}")
    private String casServerLoginUrl;

    @Value("${cas.client.server.name}")
    private String casClientServerName;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(authenticationFilter());
        final Map<String, String> initParams = new HashMap<>(2);
        initParams.put("casServerLoginUrl", casServerLoginUrl);
        initParams.put("serverName", casClientServerName);
        registration.setInitParameters(initParams);
        registration.setOrder(2);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean cas30ProxyReceivingTicketValidationFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(cas30ProxyReceivingTicketValidationFilter());
        registration.addUrlPatterns("/*");
        final Map<String, String> initParams = new HashMap<>(2);
        initParams.put("casServerUrlPrefix", casServerUrlPrefix);
        initParams.put("serverName", casClientServerName);
        registration.setInitParameters(initParams);
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(httpServletRequestWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(3);
        return registration;
    }

    @Bean
    public FilterRegistrationBean singleSignOutFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(singleSignOutFilter());
        registration.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<>(1);
        initParameters.put("casServerUrlPrefix", casServerUrlPrefix);
        registration.setInitParameters(initParameters);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        AuthenticationFilter filter = new AuthenticationFilter();
        return filter;
    }

    @Bean
    public Cas30ProxyReceivingTicketValidationFilter cas30ProxyReceivingTicketValidationFilter() {
        Cas30ProxyReceivingTicketValidationFilter filter = new Cas30ProxyReceivingTicketValidationFilter();
        filter.setRedirectAfterValidation(true);
        filter.setUseSession(true);
        filter.setServerName(casClientServerName);
        return filter;
    }

    @Bean
    public HttpServletRequestWrapperFilter httpServletRequestWrapperFilter() {
        HttpServletRequestWrapperFilter filter = new HttpServletRequestWrapperFilter();
        return filter;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter filter = new SingleSignOutFilter();
        filter.setCasServerUrlPrefix(casServerUrlPrefix);
        filter.setIgnoreInitConfiguration(true);
        return filter;
    }

    @Bean
    public ServletListenerRegistrationBean singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<java.util.EventListener> registration = new ServletListenerRegistrationBean<>();
        SingleSignOutHttpSessionListener listener = new SingleSignOutHttpSessionListener();
        registration.setListener(listener);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
