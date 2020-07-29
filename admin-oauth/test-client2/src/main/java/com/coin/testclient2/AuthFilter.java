package com.coin.testclient2;

import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName AuthFilter
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-29 16:44
 * @Version V1.0
 **/
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hsr = (HttpServletRequest) request;

        String authorization = hsr.getHeader("Authorization");
        String accessToken = hsr.getHeader("access_token");
        String paramToken = hsr.getParameter("access_token");
        if(StringUtils.isEmpty(authorization) && StringUtils.isEmpty(accessToken) && StringUtils.isEmpty(paramToken)) {
            if(hsr.getServletPath().equals("/callback")) {
                chain.doFilter(request, response);
            } else {
                throw new RuntimeException("未登录");
            }
        }
        chain.doFilter(request, response);
    }
}
