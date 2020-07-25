package com.coin.testclient2;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName TestController
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-24 16:19
 * @Version V1.0
 **/
@Controller
public class TestController {

    public OAuth2RestOperations restTemplate;

    @GetMapping("test")
    @ResponseBody
    public String test(HttpServletRequest request) {
        return request.getUserPrincipal().getName();
    }

    @GetMapping("user")
    @ResponseBody
    public Authentication user(Authentication user) {
        return user;
    }


    @GetMapping("accessToken")
    @ResponseBody
    public String accessToken(HttpServletRequest request) {
        return (String) request.getAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE);
    }
}
