package com.coin.uua.client.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.CollectionUtils;

import javax.servlet.FilterConfig;
import java.util.*;

/**
 * @ClassName CasClientConfig
 * @Description: Cas客户端配置类
 * @Author kh
 * @Date 2020-07-30 19:25
 * @Version V1.0
 **/
// @Configuration
public class CasClientConfig {

    @Value("${cas.server.url.prefix}")
    private String casServerUrlPrefix;

    @Value("${cas.server.url.login}")
    private String casServerLoginUrl;

    @Value("${cas.server.url.logout}")
    private String casServerLogoutUrl;

    @Value("${cas.client.server.name}")
    private String casClientServerName;

    @Value("${cas.client.server.url.prefix}")
    private String casClientServerPrefix;

    private CasClientConfigurer casClientConfigurer;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(authenticationFilter());
        final Map<String, String> initParams = new HashMap<>(2);
        initParams.put("casServerLoginUrl", casServerLoginUrl);
        initParams.put("serverName", casClientServerName);
        registration.setInitParameters(initParams);
        registration.setOrder(4);
        // registration.addUrlPatterns("/*");
        if(this.casClientConfigurer != null) {
            casClientConfigurer.configureAuthenticationFilter(registration);
        }
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
        // initParams.put("ignorePattern", "");
        // initParams.put("ignoreUrlPatternType", "com.coin.uua.client.config.SimpleUrlPatternMatcherStrategy");
        registration.setInitParameters(initParams);
        registration.setOrder(5);
        if(this.casClientConfigurer != null) {
            casClientConfigurer.configureValidationFilter(registration);
        }

        return registration;
    }

    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(httpServletRequestWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(6);
        if(this.casClientConfigurer != null) {
            casClientConfigurer.configureHttpServletRequestWrapperFilter(registration);
        }

        return registration;
    }

    @Bean
    public FilterRegistrationBean logOutFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();//new SecurityContextLogoutHandler()
        LogoutFilter logoutFilter = new LogoutFilter(casServerLoginUrl + "/logout?service=" + casClientServerName, new SecurityContextLogoutHandler());
        filterRegistration.setFilter(logoutFilter);
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/logout");
        filterRegistration.addInitParameter("casServerUrlPrefix", casServerLoginUrl);
        filterRegistration.addInitParameter("serverName", casClientServerName);
        filterRegistration.setOrder(2);
        return filterRegistration;
    }

    @Bean
    public FilterRegistrationBean singleSignOutFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(singleSignOutFilter());
        registration.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<>(1);
        initParameters.put("casServerUrlPrefix", casServerUrlPrefix);
        initParameters.put("serverName", casClientServerName);
        registration.setInitParameters(initParameters);
        registration.setOrder(3);
        return registration;
    }

    @Bean
    public FilterRegistrationBean casAssertionThreadLocalFilter(){
        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new AssertionThreadLocalFilter());
        authenticationFilter.setOrder(7);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        // authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
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
    public AssertionThreadLocalFilter assertionThreadLocalFilter() {
        AssertionThreadLocalFilter threadLocalFilter = new AssertionThreadLocalFilter();

        return threadLocalFilter;
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
