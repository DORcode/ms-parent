package com.coin.testclient2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
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

    @GetMapping("user")
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
     * 认证服务器的界面不分离，不分离比较简单，开发时，麻烦，体验差，分离，登录需要由form改造成ajax,改造username...即可？还有一些处理
     *
     * 或者权限做成一个单独的应用，前后端分离，
     *
     * 由前端自己判断是否登录，根据access_token和过期时间判断， 如果未登录，则登录
     *
     * 对于已经登录，过段时间再次使用，怎么判断是登录过的用户？把 refresh_token过期时间发给关段，
     * 再次登录时，判断如果fresh_token未过期，直接刷新，如果过期，直接登录，如果刷新时，返回的状态为fresh_token异常则重新重定向登录。
     *
     * 过滤，如果未登录，直接返回，状态，提醒前端发起登录，如果token快过期，比如剩下2分钟，
     * 在session中记录已经提醒，提醒的具体时间，并判断当前时间，如果相差很大，就再次拦截，对于过期，刷新access_token后，拦截回前端，特殊识别码
     *
     * 不需要 使用接口，在过滤器中定义即可。
     *
     * 简单的办法，是用户侧自己去获取code，
     *
     *
     * 另一种方式，在后端，加一个页面，前端如果发现未登录，或者fresh_token过期，访问这个页面，并带参数，当前访问地址，后端登录验证后，重定向到该页面。
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

    @GetMapping("skip")
    public void skip(Model mv, Authentication user, HttpServletResponse response) throws IOException {
        if(user.isAuthenticated()) {
            response.sendRedirect("http://localhost:3001/test");
        } else {

        }
//            mv.addAttribute("isLogin", true);
//        } else {
//            mv.addAttribute("isLogin", false);
//        }
//        return "/skip";
    }
}
