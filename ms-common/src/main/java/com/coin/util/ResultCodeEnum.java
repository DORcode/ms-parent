package com.coin.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName ResultCodeEnum
 * @Description: TODO
 * @Author kh
 * @Date 2020/1/16 17:49
 * @Version V1.0
 **/

public enum ResultCodeEnum {
    SUCCESS(200, "请求成功"),

    LOGIN_SUCCESS(2001, "登录成功"),
    LOGINED(2002, "已登录！"),

    LOGINOUT_SUCCESS(2010, "登出成功"),
    UNLOGIN(2011, "未登录"),
    TOKEN_EXPIRE(2020, "token过期"),

    LOGON_SUCCESS(2030, "注册成功"),
    LOGON_FAIL(2031, "注册失败"),

    CHG_PWD_SUCCESS(2032, "修改密码成功"),

    VERIFY_CODE_G_ERROR(2033, "生成验证码异常，请重试"),
    VERIFY_CODE_SUCCESS(2034, "输入验证码正确"),
    VERIFY_CODE_FAIL(2035, "输入验证码错误"),
    VERIFY_CODE_NULL(2036, "验证码不应该为空"),

    VERIFY_CODE_EXCEPT(2037, "验证码异常，请重新选择并输入"),
    VERIFY_CODE_EXPIRE(2038, "验证码已过期，请重新选择并输入"),

    PARAM_IS_NULL(2050, "参数不应该为空"),

    SAVE_FAIL(2060, "保存失败"),
    SAVE_SUCCESS(2061, "保存成功"),

    DELETE_FAIL(2070, "删除失败"),
    DELETE_SUCCESS(2070, "删除成功"),

    UPDATE_FAIL(2080, "更新失败"),
    UPDATE_SUCCESS(2091, "更新成功"),

    UNUNIQUE(2100, "数据库已存在，不唯一"),

    USER_NOT_EXIST(2101, "用户不存在"),
    OLD_PASSWORD(2102, "原密码输入不正确"),

    ACCOUNT_ERROR(3001, "帐户或密码错误！"),
    PARAM_ERROR(4001, "参数错误"),
    LIST_EMPTY_ERROR(4002, "列表不应该为！"),
    CODE_NULL(4003, "编码为空"),
    SERVER_ERROR(5001, "服务端异常！"),
	
    DEMANDINFO_UPDATE_FAIL(7001, "发文信息错误,更新失败"),
    DEMANDINFO_LIST_FAIL(7002, "分页查询发文参数错误,查询失败"),
	
    FILE_DOWNLOAD_FAIL(8001, "文件信息错误,下载失败"),
    FILE_STREAM_FAIL(8002, "文件流错误,下载失败"),
    FILE_DELETE_FAIL(8003, "文件信息错误,删除失败"),
    FILE_LIST_FAIL(8004, "分页查询文件参数错误,查询失败"),
	
	FASTDFS_UPLOAD_FAIL(9001, "fastdfs上传失败"),
	FASTDFS_DOWNLOAD_FAIL(9002, "fastdfs下载失败"),
	FASTDFS_DELETE_FAIL(9003, "fastdfs删除失败");

    @Getter
    @Setter
    private Integer code;
    @Getter
    @Setter
    private String msg;

    ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
}
