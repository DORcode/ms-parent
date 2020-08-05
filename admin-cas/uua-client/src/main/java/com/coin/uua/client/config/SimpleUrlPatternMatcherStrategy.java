package com.coin.uua.client.config;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

/**
 * @ClassName SimpleUrlPatternMatcherStrategy
 * @Description: TODO
 * @Author kh
 * @Date 2020-08-05 11:14
 * @Version V1.0
 **/
public class SimpleUrlPatternMatcherStrategy implements UrlPatternMatcherStrategy {

    // 根据

    @Override
    public boolean matches(String url) {
        return false;
    }

    @Override
    public void setPattern(String pattern) {

    }
}
