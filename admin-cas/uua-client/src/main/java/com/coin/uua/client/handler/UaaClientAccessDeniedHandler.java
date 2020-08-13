package com.coin.uua.client.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName UaaClientAccessDeniedHandler
 * @Description: TODO
 * @Author kh
 * @Date 2020-08-13 11:32
 * @Version V1.0
 **/
public class UaaClientAccessDeniedHandler implements AccessDeniedHandler {

    String errorPage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        String path = request.getServletPath();
        // 如果是api开始的，是ajax请求，返回消息，重新登录
        if (request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equals(
                "XMLHttpRequest")) || !path.contains("callback")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getOutputStream().write(objectMapper.writeValueAsBytes("权限不足"));
        } else {
            if(errorPage!=null){
                request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
                dispatcher.forward(request, response);
            }else{
                response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
            }
        }
    }
}
