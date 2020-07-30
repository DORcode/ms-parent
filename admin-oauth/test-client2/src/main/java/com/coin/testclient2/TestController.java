package com.coin.testclient2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName TestController
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-24 16:19
 * @Version V1.0
 **/
@Controller
public class TestController {

    @Qualifier("restTemplate")
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("test")
    @ResponseBody
    public String test(HttpServletRequest request) {
        return request.getUserPrincipal().getName();
    }

    @GetMapping("/api/user")
    @ResponseBody
    public Authentication user(Authentication user) {
        return user;
    }

    @GetMapping("front")
    public void front(HttpServletResponse response) throws IOException {
        // return "redirect:http://localhost:3001/test";
        response.sendRedirect("http://localhost:3001/test");
    }


    /**
     * @MethodName accessToken
     * @Description
     *
     * @param code
     * @param state
     * @param request
     * @return java.lang.Object
     * @throws
     * @author kh
     * @date 2020-07-27 17:28
     */
    @GetMapping("accessToken")
    @ResponseBody
    public Object accessToken(String code, String state, HttpServletRequest request) {
        if (code != null) {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("client_id", "ms-client");
            map.add("client_secret", "aa");
            map.add("redirect_uri", "http://localhost:3001/hw");
            map.add("grant_type", "authorization_code");
            map.add("state", state);
            Map resp = restTemplate.postForObject("http://localhost:8090/oauth/token", map, Map.class);
            System.out.println("resp = " + resp);
            String access_token = (String) resp.get("access_token");
            String refresh_token = (String) resp.get("refresh_token");
//            System.out.println(access_token);
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Authorization", "Bearer " + access_token);
//            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
//            ResponseEntity<String> entity = restTemplate.exchange("http://localhost:8091/admin/hello", HttpMethod.GET, httpEntity, String.class);
//            model.addAttribute("msg", entity.getBody());
//            model.addAttribute("refresh_token", refresh_token);
//            model.addAttribute("access_token", access_token);
            return resp;
        }

        return null;
    }

    @GetMapping("callback")
    public void callback(Model mv, Authentication user, HttpServletResponse response) throws IOException {

        if(user != null && user.isAuthenticated()) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) user.getDetails();
            String address = String.format("http://localhost:3001/test?access_token=%s&token_type=%s&sessionId=%s",
                    details.getTokenValue(), details.getTokenType(), details.getSessionId());
            response.sendRedirect(address);
        } else {
            // TokenEndpointAuthenticationFilter
            // AuthorizationServerTokenServices
            // SavedRequestAwareAuthenticationSuccessHandler
        }
//            mv.addAttribute("isLogin", true);
//        } else {
//            mv.addAttribute("isLogin", false);
//        }
//        return "/skip";
    }
}