package com.micro.ykh.constant;

/**
 * @ClassName FwtConstant
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/22 11:17
 * @Version 1.0
 **/
public class FwtConstant {

    /**
     * sms应用名
     */
    public final static String APPLICATION_NAME = "fuwutong";

    /**
     * 手机区域号
     */
    public final static String PHONE_HEADER = "86";

    /**
     * 获得token的URL地址
     */
    public final static String GET_TOKEN_URL = "http://user-center/user/oauth/token";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 6;

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * sms验证码 redis key
     */
    public static final String SMS_CODE_KEY = "sms_codes:";

    /**
     * 用户刷新码
     */
    public static final String REFRESH_TOKEN_KEY = "refresh_token:";

    /**
     * RSA密钥明文
     */
    public static final String RSA_PLAIN_TEXT = "xiaohui";

    /**
     * RSA公钥路径
     */
    public static final String RSA_KEY_PATH = "jwt.jks";

    /**
     * RSA-key别名
     */
    public static final String RSA_KEY_ALIA = "jwt";

    /**
     * des加密key
     */
    public static final String DES_PASSWORD_KEY = "abcd";

    /**
     * des加密vi
     */
    public static final String DES_PASSWORD_VI = "12344321";

    /**
     * 字节编码
     */
    public static final String CHARSET = "utf-8";

    /**
     * ISO8859编码跟linux个版本默认字符编码相匹配
     */
    public static final String CHARSET_ISO8859 = "ISO8859-1";

    /**
     * token黑名单
     */
    public static final String TOKEN_BLACK_LIST_KEY = "black_list_token:";
    /**
     * 公钥开头
     */
    public static final String PUBLIC_KEY_FRONT = "\\-*BEGIN PUBLIC KEY\\-*";

    /**
     * 公钥结尾
     */
    public static final String PUBLIC_KEY_END = "\\-*END PUBLIC KEY\\-*";

    /**
     * 应用名称
     */
    public static final String APP_NAME = "福物通";

    /** 校验返回结果码 */
    public final static String UNIQUE = "0";
    public final static String NOT_UNIQUE = "1";
    /**
     * 平台端管理员ID
     */
    public final static Integer PLATFORM_USER_ID = 1;

    /**
     * 平台端获得菜单的请求路径
     */
    public final static String PLATFORM_GET_ROUTES_URL = "getRoutes";

    /**
     * 平台端获得用户信息的请求路径直接放过
     */
    public final static String PLATFORM_GET_USER_INFO_URL = "getInfo";

    /**
     * 平台端数据字典访问路径
     */
    public final static String PLATFORM_DICT_URL = "dict";
    /**
     * 店铺端测试帐号
     */
    public final static String SHOP_APP_TEST_ACCOUNT = YkhProperties.getInstance().getValue("shopapp.test.account","");

    /**
     * 客户端测试帐号
     */
    public final static String CLIENT_APP_TEST_ACCOUNT = YkhProperties.getInstance().getValue("clientapp.test.account","");


}
