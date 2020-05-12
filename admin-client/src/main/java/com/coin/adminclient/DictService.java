package com.coin.adminclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName DictService
 * @Description: TODO
 * @Author kh
 * @Date 2020/4/30 13:54
 * @Version V1.0
 **/
@FeignClient(name = "ms-dict")
public interface DictService {

    @GetMapping("/api/sysdict/selectSysDict/{id}")
    public Object selectSysDictById(@PathVariable("id") String id);

}
