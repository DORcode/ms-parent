package com.coin.msclient;

import com.coin.msclient.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description: TODO
 * @Author kh
 * @Date 2020/4/16 15:09
 * @Version V1.0
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("hello")
    public String hello() {
        return testService.getName();
    }
}
