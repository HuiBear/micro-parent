package com.micro.ykh.common.sms;

/**
 * @author lzb
 * @since 2020-12-10
 */
public class CodeMsg {
    private int code;
    private String msg;

    /**
     * 通用的错误码
     */
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
    public static CodeMsg ACCESS_LIMIT_REACHED= new CodeMsg(500104, "访问太频繁！");
    public static CodeMsg SERVER_INNER_ERROR= new CodeMsg(500105, "服务器内部错误！");

    /**
     * 成功的  200
     */
    public static CodeMsg LOGIN_OUT_SUCCESS = new CodeMsg(200, "登出成功！");
    public static CodeMsg SEND_SUCCESS = new CodeMsg(200, "发送成功！");
    public static CodeMsg RETURN_SUCCESS = new CodeMsg(200, "返回成功！");


    /**
     * 登录模块 5002XX
     */
    public static CodeMsg SESSION_ERROR = new CodeMsg(500200, "Session不存在或者已经失效！");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500201, "登录密码不能为空！");
    public static CodeMsg MOBILE_INVALID = new CodeMsg(500202, "请输入有效的手机号码！");
    public static CodeMsg MOBILE_CODE_FAIL_SEND = new CodeMsg(500203, "短信验证码发送失败！");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500204, "手机号不存在！");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500205, "密码错误！");
    public static CodeMsg LOGIN_TYPE_ERROR = new CodeMsg(500206, "暂不支持此登录类型！");
    public static CodeMsg LOGIN_OUT_EMPTY = new CodeMsg(500207, "登出帐号不能为空！");

    /**
     * XX模块 5003XX
     */
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500300, "订单不存在！");

    private CodeMsg( ) {
    }

    private CodeMsg( int code,String msg ) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }
}
