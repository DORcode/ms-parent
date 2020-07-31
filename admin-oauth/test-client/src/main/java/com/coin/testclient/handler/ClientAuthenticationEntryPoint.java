package com.coin.testclient.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName ClientAuthenticationEntryPoint
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-31 14:15
 * @Version V1.0
 **/
@Component
public class ClientAuthenticationEntryPoint implements AuthenticationEntryPoint {

    String errorPage = null;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals(
                "XMLHttpRequest"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getOutputStream().write(objectMapper.writeValueAsBytes("权限不足"));
        } else {
            if(errorPage != null){
                request.setAttribute(WebAttributes.ACCESS_DENIED_403, authException);

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
                dispatcher.forward(request, response);
            }else{
                response.sendError(HttpServletResponse.SC_FORBIDDEN, authException.getMessage());
            }

        }
    }
}
