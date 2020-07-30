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
     *
     * 由前端自己判断是否登录，根据access_token和过期时间判断， 如果未登录，则登录
     *
     * 对于已经登录，过段时间再次使用，怎么判断是登录过的用户？把 refresh_token过期时间发给前端，
     * 再次登录时，判断如果fresh_token未过期，直接刷新，如果过期，直接登录，如果刷新时，返回的状态为fresh_token异常则重新重定向登录。
     *
     * 过滤，如果未登录，直接返回，状态，提醒前端发起登录，如果token快过期，比如剩下2分钟，
     * 在session中记录已经提醒，提醒的具体时间，并判断当前时间，如果相差很大，就再次拦截，对于过期，刷新access_token后，拦截回前端，特殊识别码
     *
     *
     * 另一种方式，在后端，加一个页面，前端如果发现未登录，或者fresh_token过期，访问这个页面，并带参数，当前访问地址，后端登录验证后，重定向到该页面。
     *
     * client是过滤方式，，对其进行改写
     * refresh_token，
     * 服务端，加入ajax登录验证方式，过滤是根据什么判断是本系统登录，还是其它系统登录。
     *
     * 前端，未登录，调用后端专门用于重定向的接口，登录后，重写向到后端login接口，
     * client后端自动去获取token,对于token获取成功，将重定向修改为重写向到前端的专门地址
     * （是否携带access_token，client后端是否会判断header或parameter中有token，或者在跨域下，后端保存，如果行不通，则改造为通过token来判断）
     *
     * 关键是要验证，access_token、或者后端保存多终端正常
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
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) user.getDetails();
        if(user.isAuthenticated()) {
            String address = String.format("http://localhost:3002/test?access_token=%s&token_type=%s&sessionId=%s",
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
