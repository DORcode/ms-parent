package com.coin.uua.client;

import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.core.Authentication;
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
    public void callback(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println("authentication = " + authentication);
        Assertion assertion = AssertionHolder.getAssertion();
        System.out.println("assertion = " + assertion);
        String address = String.format("http://localhost:3002/test?access_token=%s&token_type=%s&sessionId=%s");
        response.sendRedirect(address);
    }
}
