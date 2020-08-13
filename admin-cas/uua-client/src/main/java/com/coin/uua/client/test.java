package com.coin.uua.client;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.AntPathMatcher;

import java.security.SecureRandom;

/**
 * @ClassName test
 * @Description: TODO
 * @Author kh
 * @Date 2020-08-13 16:39
 * @Version V1.0
 **/
public class test {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String aaaa = encoder.encode("aaaa");
        System.out.println("aaaa = " + aaaa);

        AntPathMatcher requestMatcher = new AntPathMatcher();

        boolean match = requestMatcher.match("/aaa/**", "/aaa/aaa/1/2/3");
        System.out.println("match = " + match);
    }
}
