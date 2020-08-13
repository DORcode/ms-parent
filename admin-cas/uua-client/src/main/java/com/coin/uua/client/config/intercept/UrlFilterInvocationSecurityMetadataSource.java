package com.coin.uua.client.config.intercept;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * @ClassName UrlFilterInvocationSecurityMetadataSource
 * @Description: TODO
 * @Author kh
 * @Date 2020-08-13 18:48
 * @Version V1.0
 **/
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    /**
     * 根据url查询
     */

    AntPathMatcher requestMatcher = new AntPathMatcher();

    public UrlFilterInvocationSecurityMetadataSource() {
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String requestUrl = fi.getRequestUrl();
        // 根据地址查询
        if(true) {
            SecurityConfig.createList("role_admin", "role_delete", "role_manage");
        }

        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
