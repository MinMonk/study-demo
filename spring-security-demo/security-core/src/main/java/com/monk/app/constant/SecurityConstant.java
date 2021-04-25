package com.monk.app.constant;

/**
 * @ClassName SecurityConstant
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/6
 * @Version V1.0
 **/
public class SecurityConstant {

    /**
     * 图形验证码在SESSION中的SESSION_KEY
     */
    public static final String SESSION_IMAGE_CODE_KEY = "SESSION_CODE_KEY_FOR_IMAGE";

    /**
     * 短信验证码在SESSION中的SESSION_KEY
     */
    public static final String SESSION_SMS_CODE_KEY = "SESSION_CODE_KEY_FOR_SMS";

    /**
     * 默认登陆界面
     */
    public static final String DEFAULT_LOGIN_PAGE = "/default-login.html";

    /**
     * 退出请求地址
     */
    public static final String DEFAULT_SIGNOUT_URL = "/signOut";

    /**
     * 默认退出页面
     */
    public static final String DEFAULT_SIGNOUT_PAGE = "/default-signOut.html";

    /**
     * 默认注册页面
     */
    public static final String DEFAULT_REGISTER_PAGE = "/default-register.html";

    /**
     * 默认的处理验证码的url前缀
     */
    public static final String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";

    /**
     * 当请求需要身份认证时，默认跳转的url
     */
    public static final String DEFAULT_UN_AUTHENTICATION_URL = "/authentication/require";

    /**
     * 默认的用户名密码登录请求处理url
     */
    public static final String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";

    /**
     * 手机短信验证方式登录请求处理url
     */
    public static final String DEFAULT_MOBILE_LOGIN_PROCESSING_URL_FORM = "/authentication/mobile";

    /**
     * 默认的注册请求URL
     */
    public static final String DEFAULT_REGISTER_URL = "/user/register";

    /**
     * Session失效跳转页面地址
     */
    public static final String SESSION_INVALID_URL = "/session/invalid";

    /**
     * 验证图片验证码时，HTTP请求中默认的携带图片验证码信息的参数名称
     */
    public static final String DEFAULT_PARAMETER_NAME_IMAGE_CODE = "imageCode";

    /**
     * 验证短信验证码时，HTTP请求中默认的携带短信验证码信息的参数名称
     */
    public static final String DEFAULT_PARAMETER_NAME_SMS_CODE = "smsCode";
}
