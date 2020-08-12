package com.coin.uua.client;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CallbackController
 * @Description: TODO
 * @Author kh
 * @Date 2020-08-12 10:00
 * @Version V1.0
 **/
@Controller
public class CallbackController {

    @GetMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        String address = String.format("http://localhost:3002/test?access_token=%s&token_type=%s&sessionId=%s");
        response.sendRedirect(address);
    }
}
