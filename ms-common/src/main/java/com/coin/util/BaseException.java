package com.coin.util;

import lombok.Data;

/**
 * @ClassName BaseException
 * @Description: TODO
 * @Author kh
 * @Date 2020/1/16 17:58
 * @Version V1.0
 **/
@Data
public class BaseException extends RuntimeException {

    private Integer code;

    private String msg;

    public BaseException() {
    }

    public BaseException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(ResultCodeEnum code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }


}
