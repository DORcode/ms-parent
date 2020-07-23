package com.coin.resourceserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @ClassName ResourceServerConfig
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-20 17:24
 * @Version V1.0
 **/
@Configuration
@EnableWebSecurity
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
        resources.resourceId("test").tokenServices(tokenService());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/messages/**")
            .authorizeRequests()
            .antMatchers("/messages/**").access("#oauth2.hasScope('message.read')")
            .antMatchers("/admin/**").hasRole("admin")
            .anyRequest().authenticated();
    }

    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices services = new RemoteTokenServices();
        services.setCheckTokenEndpointUrl("http://localhost:8090/oauth/check_token");
        services.setClientId("ms-client");
        services.setClientSecret("aa");
        return services;
    }
}
