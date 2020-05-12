package com.coin.adminclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description: TODO
 * @Author kh
 * @Date 2020/4/16 21:44
 * @Version V1.0
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private DictService dictService;

    @GetMapping("hello")
    public String hello() {
        return testService.hello();
    }

    @GetMapping("/")
    public String test() {
        return "new2";
    }

    @GetMapping("/dict")
    public Object dict() {
        return dictService.selectSysDictById("1");
    }


}
