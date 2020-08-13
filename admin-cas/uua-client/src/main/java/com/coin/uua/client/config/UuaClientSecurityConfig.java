/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coin.uua.client.config;

import com.coin.uua.client.UaaUserDetailsService;
import com.coin.uua.client.handler.UaaClientAccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Cas30ProxyTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author Joe Grandja
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UuaClientSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${cas.server.url.prefix}")
	private String casServerUrlPrefix;

	@Value("${cas.server.url.login}")
	private String casServerLoginUrl;

	@Value("${cas.server.url.logout}")
	private String casServerLogoutUrl;

	@Value("${cas.client.server.name}")
	private String casClientServerName;

	@Value("${cas.client.server.url.prefix}")
	private String casClientServerUrlPrefix;

	@Value("${cas.client.server.login}")
	private String login;
	@Value("${cas.client.server.logout}")
	private String logout;

	@Value("${cas.client.server.callback}")
	private String callback;

	private String errorPage;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(casAuthenticationProvider());
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/bootstrap/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.logout()
				.and()
				.authorizeRequests()
				.anyRequest().authenticated()
				.and()
				.csrf().disable().cors().configurationSource(corsConfigurationSource());

		http.exceptionHandling()
				.authenticationEntryPoint(casAuthenticationEntryPoint())
				.accessDeniedHandler(new UaaClientAccessDeniedHandler())
				.and()
				.addFilter(casAuthenticationFilter())
				.addFilterBefore(casLogoutFilter(), LogoutFilter.class)
				.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);
	}

	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		//Cas Server的登录地址
		casAuthenticationEntryPoint.setLoginUrl(casServerLoginUrl);
		//service相关的属性
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		// 设置回调的service路径，此为主页路径
		//Cas Server认证成功后的跳转地址，这里要跳转到我们的Spring Security应用，
		//之后会由CasAuthenticationFilter处理，默认处理地址为/j_spring_cas_security_check
		if(StringUtils.isEmpty(callback)) {
			serviceProperties.setService(casClientServerUrlPrefix + login);
		} else {
			serviceProperties.setService(casClientServerUrlPrefix + callback);
		}

		// 对所有的未拥有ticket的访问均需要验证
		serviceProperties.setAuthenticateAllArtifacts(true);
		return serviceProperties;
	}

	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfigurationSource source =   new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.setAllowCredentials(true);
		((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**",corsConfiguration); //配置允许跨域访问的url
		return source;
	}

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		casAuthenticationFilter.setAuthenticationManager(authenticationManager());
		casAuthenticationFilter.setServiceProperties(serviceProperties());
		//指定处理地址，不指定时默认将会是“/j_spring_cas_security_check”
		casAuthenticationFilter.setFilterProcessesUrl(casServerLoginUrl);
		casAuthenticationFilter.setContinueChainBeforeSuccessfulAuthentication(false);
		casAuthenticationFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/callback"));
		// casAuthenticationFilter.setAuthenticationFailureHandler();
		return casAuthenticationFilter;
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider.setAuthenticationUserDetailsService(uaaUserDetailsService());
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		casAuthenticationProvider.setTicketValidator(cas30ServiceTicketValidator());
		casAuthenticationProvider.setKey("casAuthenticationProviderKey");
		return casAuthenticationProvider;
	}

	@Bean
	public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> uaaUserDetailsService(){
		return new UaaUserDetailsService();
	}

	/**
	 * 配置Ticket校验器
	 * @return
	 */
	@Bean
	public Cas30ProxyTicketValidator cas30ServiceTicketValidator() {
		// 配置上服务端的校验ticket地址
		Cas30ProxyTicketValidator ticketValidator = new Cas30ProxyTicketValidator(casServerUrlPrefix);
		return ticketValidator;
	}

	/**
	 * 单点注销，接受cas服务端发出的注销session请求
	 * @see
	 * @return
	 */
	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(casServerUrlPrefix);
		singleSignOutFilter.setIgnoreInitConfiguration(true);
		return singleSignOutFilter;
	}

	/**
	 * 单点请求CAS客户端退出Filter类
	 * 请求/logout，转发至CAS服务端进行注销
	 */
	@Bean
	public LogoutFilter casLogoutFilter() {
		// 设置回调地址，以免注销后页面不再跳转
		LogoutFilter logoutFilter = new LogoutFilter(casServerLogoutUrl, new SecurityContextLogoutHandler());
		logoutFilter.setFilterProcessesUrl(logout);
		return logoutFilter;
	}

	private AuthenticationFailureHandler failureHandler() {
		return (request, response, e) -> {
			response.setStatus(INTERNAL_SERVER_ERROR.OK.value());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.getWriter().append("error json response");
		};
	}
}
