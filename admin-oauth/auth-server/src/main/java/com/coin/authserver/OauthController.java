package com.coin.authserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName OauthController
 * @Description: TODO
 * @Author kh
 * @Date 2020-07-21 18:03
 * @Version V1.0
 **/
@Controller
@RequestMapping
public class OauthController {

    @RequestMapping("/oauth/error")
    public void oauthError() {

    }
}
