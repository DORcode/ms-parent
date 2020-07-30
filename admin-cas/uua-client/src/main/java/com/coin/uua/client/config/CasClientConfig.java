package com.coin.uua.client.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

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

    @Value("{cas.server.url.login}")
    private String casServerLoginUrl;

    @Value("${cas.client.server.name}")
    private String casClientServerName;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(authenticationFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setCasServerLoginUrl(casServerLoginUrl);
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

    @EventListener
    public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener() {
        SingleSignOutHttpSessionListener listener = new SingleSignOutHttpSessionListener();
        return listener;
    }

}
