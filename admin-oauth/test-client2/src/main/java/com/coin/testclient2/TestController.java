package com.coin.testclient2;

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

    @GetMapping("test")
    @ResponseBody
    public String test(HttpServletRequest request) {
        return request.getUserPrincipal().getName();
    }
}
