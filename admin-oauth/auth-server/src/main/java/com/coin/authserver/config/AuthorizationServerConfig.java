package com.coin.authserver.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.parser.TokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AuthServerConfig
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-20 16:16
 * @Version V1.0
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

//    @Autowired
//    DataSource dataSource;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 限制客户端访问认证接口的权限
        security.allowFormAuthenticationForClients();
        // security.checkTokenAccess("isAuthenticated()");
        security.tokenKeyAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
            .inMemory()
            .withClient("ms-client")
            .authorizedGrantTypes("authorization_code", "refresh_token")
            .scopes("all")
            .secret(passwordEncoder.encode("aa"))
            .accessTokenValiditySeconds(7000)
                // .refreshTokenValiditySeconds(7000)
            .redirectUris("http://localhost:8280/login").autoApprove(true);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager)
//                .tokenServices(tokenService())
//            .userApprovalHandler(userApprovalHandler())
//            .accessTokenConverter(accessTokenConverter());
        endpoints.tokenStore(jwtTokenStore());
        endpoints.accessTokenConverter(accessTokenConverter());
    }

    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(jwtTokenStore());
        services.setSupportRefreshToken(true);
        services.setReuseRefreshToken(true);
        services.setAccessTokenValiditySeconds(60 * 60 * 2);
        // services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
        return services;
    }


    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("cjs");   //  Sets the JWT signing key
        return jwtAccessTokenConverter;
    }

//    @Bean
//    public UserApprovalHandler userApprovalHandler() {
//        ApprovalStoreUserApprovalHandler userApprovalHandler = new ApprovalStoreUserApprovalHandler();
//        userApprovalHandler.setApprovalStore(approvalStore());
//        userApprovalHandler.setClientDetailsService(this.clientDetailsService);
//        userApprovalHandler.setRequestFactory(new DefaultOAuth2RequestFactory(this.clientDetailsService));
//        return userApprovalHandler;
//    }

//    @Bean
//    public ApprovalStore approvalStore() {
//        return new InMemoryApprovalStore();
//    }

    @Bean
    public TokenStore jwtTokenStore() {
        // 可以修改为redis
        JwtTokenStore tokenStore = new JwtTokenStore(accessTokenConverter());
        // tokenStore.setApprovalStore(approvalStore());
        return tokenStore;
    }

    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder(KeyConfig.getVerifierKey())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(KeyConfig.VERIFIER_KEY_ID);
        return new JWKSet(builder.build());
    }
}
