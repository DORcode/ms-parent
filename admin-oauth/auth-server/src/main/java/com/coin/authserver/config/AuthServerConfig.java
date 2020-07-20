package com.coin.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

/**
 * @ClassName AuthServerConfig
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-20 16:16
 * @Version V1.0
 **/
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;


    // 支持clientid、secret登录认证
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    // 配置授权模式
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients
            .inMemory()
            .withClient("ms-client")
            .authorizedGrantTypes("authorization_code", "implicit", "refresh_token", "client_credentials", "password")
            .scopes("read", "write")
            .secret("")
            .redirectUris("http://localhost:8080/authorized");
//        clients
//            .withClientDetails(clientDetails())
//            .withClient("")
//            .authorizedGrantTypes("")
//            .scopes("").and();
    }

    // 配置令牌存储
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
    }

    @Bean
    public ClientDetailsService clientDetails() {
        // new JdbcClientDetailsService(dataSource);
        return null;
    }
}
