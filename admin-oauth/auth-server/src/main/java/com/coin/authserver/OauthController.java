package com.coin.authserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName OauthController
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-21 18:03
 * @Version V1.0
 **/
@Controller
public class OauthController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
