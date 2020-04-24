package com.coin.adminclient;

import org.springframework.stereotype.Component;

/**
 * @ClassName TestHystrix
 * @Description: TODO
 * @Author kh
 * @Date 2020/4/20 16:31
 * @Version V1.0
 **/
@Component
public class TestHystrix implements TestService {
    @Override
    public String hello() {
        return "服务异常";
    }
}
