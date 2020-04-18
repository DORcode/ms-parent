package com.coin.msclient.service.impl;

import com.coin.msclient.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @ClassName TestServiceImpl
 * @Description: TODO
 * @Author kh
 * @Date 2020/4/16 21:39
 * @Version V1.0
 **/
@Service
public class TestServiceImpl implements TestService {
    @Override
    public String getName() {
        return "test-成功2";
    }
}
