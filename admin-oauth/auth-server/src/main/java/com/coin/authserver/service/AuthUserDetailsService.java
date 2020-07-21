package com.coin.authserver.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @ClassName AuthUserDetailsService
 * @Description: 根据用户名来查询用户信息
 * @Author kh
 * @Date 2020-07-21 14:27
 * @Version V1.0
 **/
// @Service
public class AuthUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
