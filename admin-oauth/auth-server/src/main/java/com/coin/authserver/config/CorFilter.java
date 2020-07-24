package com.coin.authserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CorFilter
 * @Description: TODO
 * @Author kh
 * @Date 2020/3/24 18:23
 * @Version V1.0
 **/
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class CorFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, Content-Type, Accept, Pragma, Last-Modified, Cache-Control, Expires, Authorization, token");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String servletPath = request.getServletPath();
        String context = request.getContextPath();
//        if((servletPath.contains("/fileManagement") || servletPath.contains("/login")) && !servletPath.contains("/api/")) {
//            req.getRequestDispatcher(context + "/" + "/index.html").forward(req, res);
//        } else {
//            chain.doFilter(req, res);
//        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
