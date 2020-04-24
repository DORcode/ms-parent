package com.coin.adminclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName TestService
 * @Description: TODO
 * @Author kh
 * @Date 2020/4/16 21:41
 * @Version V1.0
 **/
@FeignClient(name = "ms-client", fallback = TestHystrix.class)
public interface TestService {

    @GetMapping("test/hello")
    String hello();
}
