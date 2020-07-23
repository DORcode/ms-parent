package com.coin.authserver.service;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 * @ClassName UaaClientDetailsService
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-23 14:59
 * @Version V1.0
 **/
public class UaaClientDetailsService implements ClientDetailsService {
    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        return null;
    }
}
